package backend;

import asm.*;
import asm.instruction.*;
import asm.operand.*;
import asm.section.Bss;
import asm.section.Data;
import asm.section.Rodata;
import ir.*;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.entity.constant.*;
import ir.entity.var.GlobalVar;
import ir.entity.var.LocalTmpVar;
import ir.entity.var.LocalVar;
import ir.function.*;
import ir.irType.ArrayType;
import ir.irType.IRType;
import ir.irType.IntType;
import ir.irType.VoidType;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;
import utility.Pair;
import utility.error.InternalException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author F
 * 遍历IR，转化为ASM指令
 */
public class InstSelector implements IRVisitor {
    //使用的所用物理寄存器
    PhysicalRegMap registerMap = new PhysicalRegMap();

    Program program;

    Func currentFunc;
    Function currentIRFunc;
    Block currentBlock;

    int virtualRegSpace = 0;

    int maxParamCnt = 0;

    HashMap<String, VirtualRegister> toReg;

    Imm zero = new Imm(0);

    //共用的重命名规则
    private String renameBlock(String blockName) {
        return ".L" + blockName + "_" + currentFunc.name;
    }

    /**
     * 找到对应的virtual register
     * IR上entity映射到asm的VirtualRegister
     *
     * @param entity IR entity
     * @return register
     */
    private VirtualRegister getVirtualRegister(Entity entity) {
        String name = entity.toString();
        IRType type;
        if (entity instanceof LocalVar ptr) {
            type = ptr.storage.type;
        } else if (entity instanceof LocalTmpVar tmpVar) {
            type = tmpVar.type;
        } else {
            throw new InternalException("get virtual register of unexpected entity " + entity);
        }
        boolean flag = type instanceof IntType intType &&
                (intType.typeName.equals(IntType.TypeName.BOOL)
                        || intType.typeName.equals(IntType.TypeName.TMP_BOOL));
        if (toReg.containsKey(name)) {
            return toReg.get(name);
        }
        VirtualRegister register = newVirtualReg(flag);
        toReg.put(name, register);
        return register;
    }

    private VirtualRegister newVirtualReg() {
        virtualRegSpace += 4;
        return new VirtualRegister(virtualRegSpace, 4);
    }

    private VirtualRegister newVirtualReg(boolean isBool) {
        if (isBool) {
            ++virtualRegSpace;
            return new VirtualRegister(virtualRegSpace, 1);
        } else {
            virtualRegSpace += 4;
            return new VirtualRegister(virtualRegSpace, 4);
        }
    }

    /**
     * 获得load、store需要的register的位置
     * - offset不超过12位，直接fp(值为address)+offset
     * - 超过12位，直接寻址(计算出直接地址，存到临时量reg(t0))
     * TODO:t0安全？
     *
     * @param register 栈上虚拟寄存器
     * @return <baseReg,offset>
     */
    private Pair<Register, Imm> getRegAddress(VirtualRegister register) {
        if (register.offset < (1 << 11)) {
            return new Pair<>(registerMap.getReg("fp"), new Imm(-register.offset));
        }
        PhysicalRegister t0 = registerMap.getReg("t0");
        t0.valueSize = 4;
        //原offset的基地址
        currentBlock.pushBack(
                new LoadAddrInst(t0, registerMap.getReg("fp"))
        );
        //offset
        Register num = (Register) number2operand(register.offset);
        //计算真正地址
        currentBlock.pushBack(
                new BinaryInst(t0, num, t0, BinaryInst.Opcode.sub)
        );
        return new Pair<>(t0, zero);
    }

    /**
     * %3 -> a0
     *
     * @param tmp    存结果的临时寄存器
     * @param entity mem被加载的ir entity
     */
    private void loadRegister(PhysicalRegister tmp, Entity entity) {
        /*
        全局变量
            lui	a0, %hi(a)
	        lw	a0, %lo(a)(a0)
         */
        if (entity instanceof GlobalVar globalVar) {
            currentBlock.pushBack(
                    new LoadGlobalInst(tmp, globalVar.identity)
            );
        } else {
            loadVirtualRegister(tmp, getVirtualRegister(entity));
        }
    }

    private void loadVirtualRegister(PhysicalRegister tmp, VirtualRegister des) {
        Pair<Register, Imm> pair = getRegAddress(des);
        tmp.valueSize = des.size;
        currentBlock.pushBack(
                new LoadInst(pair.getFirst(), tmp, pair.getSecond())
        );
    }

    /**
     * a0 -> %3
     *
     * @param tmp    存结果的临时寄存器
     * @param entity 存入mem的ir entity
     */
    private void storeRegister(PhysicalRegister tmp, Entity entity) {
        if (entity instanceof GlobalVar globalVar) {
            currentBlock.pushBack(
                    new StoreGlobalInst(tmp, registerMap.getReg("a4"), globalVar.identity)
            );
        } else {
            storeVirtualRegister(tmp, getVirtualRegister(entity));
        }
    }

    private void storeVirtualRegister(PhysicalRegister tmp, VirtualRegister des) {
        Pair<Register, Imm> pair = getRegAddress(des);
        currentBlock.pushBack(
                new StoreInst(tmp, pair.getFirst(), pair.getSecond())
        );
    }

    public InstSelector(Program program) {
        this.program = program;
    }

    /**
     * IRRoot
     * 访问全局变量的定义（Global指令转为全局变量表示）
     * 初始化函数
     * 一一访问函数
     * TODO:内建函数是否需要声明？怎么声明？
     *
     * @param root Mx的根
     */
    @Override
    public void visit(IRRoot root) {
        //全局变量、字符串常量
        root.globalVarDefBlock.accept(this);
        //全局变量的初始化函数
        root.globalVarInitFunction.accept(this);
        //函数
        for (Map.Entry<String, Function> function : root.funcDef.entrySet()) {
            Function func = function.getValue();
            //有函数定义的函数
            if (func.entry != null) {
                function.getValue().accept(this);
            }
        }
    }

    private void setPhysicalRegSize(PhysicalRegister reg, Entity entity) {
        if (entity.type instanceof IntType intType
                && intType.typeName.equals(IntType.TypeName.BOOL)) {
            reg.valueSize = 1;
        } else {
            reg.valueSize = 4;
        }
    }

    /**
     * 每个函数按block翻译
     * block用函数名重命名，确保名字不相同
     * 统计每个函数需要的栈空间
     * - 局部变量alloca
     * - 临时变量
     * - reg放不下的参数传递
     *
     * @param function 所有函数
     */
    @Override
    public void visit(Function function) {
        //enter function
        currentFunc = new Func(function.funcName);
        currentIRFunc = function;
        program.text.functions.add(currentFunc);
        virtualRegSpace = 8;
        maxParamCnt = 0;
        toReg = new HashMap<>();
        //顺序访问每一个block
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            currentBlock = new Block(renameBlock(block.label));
            currentFunc.funcBlocks.add(currentBlock);
            visit(block);
        }
        //exit function
        currentFunc.basicSpace = virtualRegSpace;
        currentFunc.extraParamCnt = (maxParamCnt > 8 ? maxParamCnt - 8 : 0);
        int stackSize = currentFunc.basicSpace
                + (currentFunc.extraParamCnt << 2);
        stackSize = (stackSize + 15) >> 4;
        stackSize <<= 4;
        //进入函数时的额外指令(不加入funcBlocks)
        currentFunc.entry = new Block(function.funcName);
        currentBlock = currentFunc.entry;
        PhysicalRegister sp = registerMap.getReg("sp");
        PhysicalRegister fp = registerMap.getReg("fp");
        currentBlock.pushBack(
                new ImmBinaryInst(sp, new Imm(-stackSize), sp, ImmBinaryInst.Opcode.addi)
        );
        currentBlock.pushBack(
                new StoreInst(registerMap.getReg("ra"), sp, new Imm(stackSize - 4))
        );
        currentBlock.pushBack(
                new StoreInst(fp, sp, new Imm(stackSize - 8))
        );
        currentBlock.pushBack(
                new ImmBinaryInst(sp, new Imm(stackSize), fp, ImmBinaryInst.Opcode.addi)
        );
        //入参（相当于局部变量）
        //访问函数alloca开好了virtual reg，将值传入（store）
        int i;
        for (i = 0; i < 8; ++i) {
            if (i == function.parameterList.size()) {
                break;
            }
            storeRegister(
                    registerMap.getReg("a" + i),
                    function.parameterList.get(i)
            );
        }
        PhysicalRegister t2 = registerMap.getReg("t2");
        for (; i < function.parameterList.size(); ++i) {
            Storage param = function.parameterList.get(i);
            setPhysicalRegSize(t2, param);
            currentBlock.pushBack(
                    new LoadInst(fp, t2, new Imm((i - 8) << 2))
            );
            storeRegister(t2, param);
        }
        //最后一个块
        currentBlock = currentFunc.funcBlocks.get(currentFunc.funcBlocks.size() - 1);
        //有返回值，返回值放在a0
        if (!(function.retType instanceof VoidType)) {
            entity2value(function.retVal, registerMap.getReg("a0"));
        }
        currentBlock.pushBack(
                new LoadInst(sp, registerMap.getReg("ra"), new Imm(stackSize - 4))
        );
        currentBlock.pushBack(
                new LoadInst(sp, fp, new Imm(stackSize - 8))
        );
        currentBlock.pushBack(
                new ImmBinaryInst(sp, new Imm(stackSize), sp, ImmBinaryInst.Opcode.addi)
        );
        currentBlock.pushBack(
                new RetInst()
        );
    }

    /**
     * @param entity ir
     */
    private void entity2value(Entity entity, PhysicalRegister reg) {
        setPhysicalRegSize(reg, entity);
        if (entity instanceof Constant constant) {
            Operand operand = const2operand(constant);
            if (operand instanceof Imm imm) {
                currentBlock.pushBack(
                        new LiInst(reg, (Imm) operand)
                );
            } else {
                currentBlock.pushBack(
                        new MoveInst(reg, (PhysicalRegister) operand)
                );
            }
            return;
        }
        loadRegister(reg, entity);
    }

    @Override
    public void visit(BasicBlock basicBlock) {
        //访问block内的每一个语句
        basicBlock.statements.forEach(
                stmt -> stmt.accept(this)
        );
    }

    /**
     * Alloca
     * %1 = alloca i32
     * 创建virtual register
     * 在栈上获得alloca对应的空间
     *
     * @param stmt 局部变量空间申请
     */
    @Override
    public void visit(Alloca stmt) {
        getVirtualRegister(stmt.result);
    }

    /**
     * 将int型数值转化为对应的操作数
     * 利用physical register a6计算
     *
     * @param num 数值
     * @return operand
     */
    Operand number2operand(int num) {
        //直接用imm
        if (num < (1 << 11)) {
            return new Imm(num);
        }
        //先lui，如果低位非0，addi
        else {
            PhysicalRegister a7 = registerMap.getReg("a7");
            a7.valueSize = 4;
            currentBlock.pushBack(
                    new LuiInst(a7, new Imm((num >> 12)))
            );
            if ((num & 0xFFF) != 0) {
                currentBlock.pushBack(
                        new ImmBinaryInst(
                                a7,
                                new Imm(num & 0xFFF),
                                a7,
                                ImmBinaryInst.Opcode.addi
                        )
                );
            }
            return a7;
        }
    }

    //TODO:超过int的str(value:long long?)

    /**
     * @param constant int\bool
     * @return imm\phyReg
     */
    Operand const2operand(Constant constant) {
        if (constant instanceof ConstInt constInt) {
            return number2operand(Integer.parseInt(constInt.value));
        } else if (constant instanceof ConstBool constBool) {
            if (constBool.value) {
                return new Imm(1);
            } else {
                return new Imm(0);
            }
        } else {
            throw new InternalException("can not resolve constant");
        }
    }

    /**
     * Binary
     *
     * @param stmt 二元运算
     */
    @Override
    public void visit(Binary stmt) {
        if (stmt.op1 instanceof ConstInt c1 && stmt.op2 instanceof ConstInt c2) {
            //两个常量运算
            int op1 = Integer.parseInt(c1.value);
            int op2 = Integer.parseInt(c2.value);
            int ans;
            switch (stmt.operator) {
                case add -> ans = op1 + op2;
                case sub -> ans = op1 - op2;
                case mul -> ans = op1 * op2;
                case sdiv -> ans = op1 / op2;
                case srem -> ans = op1 % op2;
                case shl -> ans = op1 << op2;
                case ashr -> ans = op1 >> op2;
                case and -> ans = op1 & op2;
                case xor -> ans = op1 ^ op2;
                case or -> ans = op1 | op2;
                default -> throw new InternalException("unexpected operator in Binary instruction");
            }
            Operand operand = number2operand(ans);//imm\phyReg
            if (operand instanceof Imm imm) {
                PhysicalRegister t1 = registerMap.getReg("t1");
                t1.valueSize = 4;
                currentBlock.pushBack(
                        new LiInst(t1, imm)
                );
                storeRegister(t1, stmt.result);
            }
            //寄存器赋值
            else {
                storeRegister((PhysicalRegister) operand, stmt.result);
            }
            return;
        }
        Operand operand1, operand2;
        if (stmt.op1 instanceof ConstInt c1) {
            operand1 = number2operand(Integer.parseInt(c1.value));
        } else {
            operand1 = registerMap.getReg("a0");
            loadRegister((PhysicalRegister) operand1, stmt.op1);
        }
        if (stmt.op2 instanceof ConstInt c2) {
            operand2 = number2operand(Integer.parseInt(c2.value));
        } else {
            operand2 = registerMap.getReg("a1");
            loadRegister((PhysicalRegister) operand2, stmt.op2);
        }
        PhysicalRegister a2 = registerMap.getReg("a2");
        a2.valueSize = 4;
        PhysicalRegister a3 = registerMap.getReg("a3");
        a3.valueSize = 4;
        if (operand1 instanceof Imm imm) {
            if (stmt.operator.equals(Binary.Operator.sub)) {
                imm = new Imm(-imm.value);
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) operand2, imm, a2, ImmBinaryInst.Opcode.addi)
                );
            } else if (stmt.operator.equals(Binary.Operator.mul) ||
                    stmt.operator.equals(Binary.Operator.sdiv) ||
                    stmt.operator.equals(Binary.Operator.srem)) {
                currentBlock.pushBack(
                        new LiInst(a3, imm)
                );
                currentBlock.pushBack(
                        new BinaryInst(a3, (Register) operand2, a2, stmt.operator)
                );
            } else {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) operand2, imm, a2, stmt.operator)
                );
            }
        } else if (operand2 instanceof Imm imm) {
            if (stmt.operator.equals(Binary.Operator.sub)) {
                imm = new Imm(-imm.value);
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) operand1, imm, a2, ImmBinaryInst.Opcode.addi)
                );
            } else if (stmt.operator.equals(Binary.Operator.mul) ||
                    stmt.operator.equals(Binary.Operator.sdiv) ||
                    stmt.operator.equals(Binary.Operator.srem)) {
                currentBlock.pushBack(
                        new LiInst(a3, imm)
                );
                currentBlock.pushBack(
                        new BinaryInst((Register) operand1, a3, a2, stmt.operator)
                );
            } else {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) operand1, imm, a2, stmt.operator)
                );
            }
        } else {
            currentBlock.pushBack(
                    new BinaryInst((Register) operand1, (Register) operand2, a2, stmt.operator)
            );
        }
        storeRegister(a2, stmt.result);
    }

    /**
     * Call
     * 函数调用
     * ----------------------------------
     * |    int b=func(1);
     * |
     * |%2 = call  i32 @_Z4func(i32 1)
     * |
     * | 	li	a0, 1           # 入参
     * |	call	_Z4func     # 调用
     * |	sw	a0, -12(s0)     # 返回值
     * ----------------------------------
     *
     * @param stmt call
     */
    @Override
    public void visit(Call stmt) {
        maxParamCnt = Math.max(stmt.parameterList.size(), maxParamCnt);
        //入参
        //若参数<=8，直接使用寄存器a0-a7传递
        PhysicalRegister t2 = registerMap.getReg("t2");
        if (stmt.parameterList.size() <= 8) {
            for (int i = 0; i < stmt.parameterList.size(); ++i) {
                entity2value(stmt.parameterList.get(i), registerMap.getReg("a" + i));
            }
        } else {
            int i;
            for (i = 0; i < 8; ++i) {
                entity2value(stmt.parameterList.get(i), registerMap.getReg("a" + i));
            }
            PhysicalRegister sp = registerMap.getReg("sp");
            for (; i < stmt.parameterList.size(); ++i) {
                Storage param = stmt.parameterList.get(i);
                setPhysicalRegSize(t2, param);
                entity2value(param, t2);
                addParamsOnStack(t2, sp, i - 8);
            }
        }
        //函数调用
        currentBlock.pushBack(
                new CallInst(stmt.function.funcName)
        );
        //存返回值
        if (!(stmt.function.retType instanceof VoidType)) {
            storeRegister(registerMap.getReg("a0"), stmt.result);
        }
    }

    //从栈顶（下）向栈底（上）分布
    private void addParamsOnStack(PhysicalRegister t2, PhysicalRegister sp, int i) {
        currentBlock.pushBack(
                new StoreInst(t2, sp, new Imm((i << 2)))
        );
    }

    /**
     * GetElementPtr
     * 手动计算偏移量寻址
     * ----------------
     * |  %3 = load ptr, ptr %1
     * |  %4 = getelementptr inbounds i32, ptr %3, i32 1    # %4被一个地址赋值（32位的大整数）
     * |  store i32 0, ptr %4
     * |
     * |	lw	a1, -12(s0)     # 首地址
     * |	li	a0, 0
     * |	sw	a0, 4(a1)       # 计算出a[1]地址4(a1)，存值（从offset取值）
     * ---------------------------------
     * bool数组：一字节寻址
     * 其余（数组+指针+类）：4字节寻址
     *
     * @param stmt GetElementPtr
     */
    @Override
    public void visit(GetElementPtr stmt) {
        //首地址lw	a1, -12(s0)
        PhysicalRegister a0 = registerMap.getReg("a0");
        a0.valueSize = 4;
        loadRegister(a0, stmt.ptrVal);
        //计算offset
        int baseSize;
        if (stmt.ptrVal.type instanceof ArrayType arrayType
                && arrayType.dimension == 1
                && arrayType.type instanceof IntType intType
                && intType.typeName.equals(IntType.TypeName.BOOL)) {
            baseSize = 1;
        } else {
            baseSize = 4;
        }
        PhysicalRegister a1 = registerMap.getReg("a1");
        a1.valueSize = 4;
        entity2value(stmt.idx, a1);
        if (baseSize == 4) {
            currentBlock.pushBack(
                    new ImmBinaryInst(a1, new Imm(2), a1, ImmBinaryInst.Opcode.slli)
            );
        }
        //计算地址
        PhysicalRegister a2 = registerMap.getReg("a2");
        a2.valueSize = 4;
        currentBlock.pushBack(
                new BinaryInst(a0, a1, a2, BinaryInst.Opcode.add)
        );
        //存值
        storeRegister(a2, stmt.result);//指向对象的指针
    }

    /**
     * Global
     * - 变量（int,bool全部作为被常数初始化的）
     * - 字符串常量
     *
     * @param stmt 全局变量的定义
     */
    @Override
    public void visit(Global stmt) {
        //字符串常量
        if (stmt.result.storage instanceof ConstString str) {
            program.globalDefs.add(
                    new Rodata(stmt.result.identity, str.value, str.strLength)
            );
            return;
        }
        //int
        if (stmt.result.storage instanceof ConstInt constInt) {
            program.globalDefs.add(
                    new Data(stmt.result.identity, constInt.value)
            );
            return;
        }
        //bool
        if (stmt.result.storage instanceof ConstBool constBool) {
            String bool = constBool.value ? "1" : "0";
            program.globalDefs.add(
                    new Data(stmt.result.identity, bool, true)
            );
            return;
        }
        //数组、类、字符串
        program.globalDefs.add(
                new Bss(stmt.result.identity)
        );
    }


    /**
     * Icmp
     * 大小比较
     * 两个imm(int\bool)比较，直接处理
     *
     * @param stmt icmp
     */
    @Override
    public void visit(Icmp stmt) {
        //两个常量
        if (stmt.op1 instanceof Constant c1
                && stmt.op2 instanceof Constant c2) {
            boolean ans;
            //bool
            if (c1 instanceof ConstBool) {
                boolean op1 = ((ConstBool) c1).value;
                boolean op2 = ((ConstBool) c2).value;
                if (stmt.cond.equals(Icmp.Cond.eq)) {
                    ans = op1 == op2;
                } else {
                    ans = op1 != op2;
                }
            } else if (c1 instanceof ConstInt) {
                int op1 = Integer.parseInt(((ConstInt) c1).value);
                int op2 = Integer.parseInt(((ConstInt) c2).value);
                switch (stmt.cond) {
                    case slt -> ans = op1 < op2;
                    case sgt -> ans = op1 > op2;
                    case sle -> ans = op1 <= op2;
                    case sge -> ans = op1 >= op2;
                    case eq -> ans = op1 == op2;
                    case ne -> ans = op1 != op2;
                    default -> throw new InternalException("unexpected operator in Icmp instruction");
                }
            } else {
                throw new InternalException("unexpected const operand in icmp");
            }
            PhysicalRegister t1 = registerMap.getReg("t1");
            t1.valueSize = 1;
            currentBlock.pushBack(
                    new LiInst(t1, new Imm(ans))
            );
            storeRegister(t1, stmt.result);
            return;
        }
        //其余
        PhysicalRegister a0 = registerMap.getReg("a0");
        PhysicalRegister a1 = registerMap.getReg("a1");
        PhysicalRegister a2 = registerMap.getReg("a2");
        a0.valueSize = a1.valueSize = 4;
        a2.valueSize = 1;
        entity2value(stmt.op1, a0);
        entity2value(stmt.op2, a1);
        if (!(stmt.cond.equals(Icmp.Cond.eq) || stmt.cond.equals(Icmp.Cond.ne))) {
            currentBlock.pushBack(
                    new CmpInst(a0, a1, a2, stmt.cond)
            );
        } else {
            currentBlock.pushBack(
                    new BinaryInst(a0, a1, a2, BinaryInst.Opcode.sub)
            );
            currentBlock.pushBack(
                    new EqualZeroInst(a2, a2, stmt.cond)
            );
        }
        storeRegister(a2, stmt.result);
    }


    /**
     * Load
     * %3 = load i32, ptr %1
     *
     * @param stmt load
     */
    @Override
    public void visit(Load stmt) {
        PhysicalRegister a0 = registerMap.getReg("a0");
        loadRegister(a0, stmt.pointer);//%1值：一个地址
        //取指针指向的对象（地址中的值）
        PhysicalRegister a1 = registerMap.getReg("a1");
        currentBlock.pushBack(
                new LoadInst(a0, a1, zero)
        );
        storeRegister(a1, stmt.result);
    }

    /**
     * Store
     * 将值存到指针指向的地址
     * -----------------------------------------------------
     * store i32 1, ptr %1
     * li	a0, 1
     * sw	a0, -12(s0)
     * ----
     * store i32 %5, ptr %1
     * sw	a0, -12(s0)
     * -----------------------------------------------------
     *
     * @param stmt store
     */
    @Override
    public void visit(Store stmt) {
        PhysicalRegister a0 = registerMap.getReg("a0");
        loadRegister(a0, stmt.pointer);//%1值：一个地址
        //取值
        PhysicalRegister a1 = registerMap.getReg("a1");
        entity2value( stmt.value,a1);
        currentBlock.pushBack(
                new StoreInst(a1, a0, zero)
        );
    }


    /**
     * ---------------------------------
     * |br i1 %11, label %12, label %13
     * |
     * |	lbu	a0, -21(s0)
     * |	andi	a0, a0, 1
     * |	beqz	a0, .LBB0_2         # false
     * |	j	.LBB0_1
     * ------------------------------------
     *
     * @param stmt branch
     */
    @Override
    public void visit(Branch stmt) {
        PhysicalRegister a0 = registerMap.getReg("a0");
        //phi
        if (stmt.phiLabel != null) {
            Entity ans = currentIRFunc.phiMap.get(stmt.index + stmt.phiLabel);
            VirtualRegister des;
            if (!toReg.containsKey(String.valueOf(stmt.index))) {
                if (ans.type instanceof IntType intType && intType.typeName.equals(IntType.TypeName.BOOL)) {
                    des = newVirtualReg(true);
                } else {
                    des = newVirtualReg();
                }
            } else {
                des = toReg.get(String.valueOf(stmt.index));
            }
            entity2value(ans, a0);
            storeVirtualRegister(a0, des);
        }
        entity2value(stmt.condition, a0);
        currentBlock.pushBack(
                new ImmBinaryInst(a0, new Imm(1), a0, ImmBinaryInst.Opcode.andi)
        );
        currentBlock.pushBack(
                new BranchInst(a0, renameBlock(stmt.trueBranch))
        );
        currentBlock.pushBack(
                new JumpInst(renameBlock(stmt.falseBranch))
        );
    }

    /**
     * Jump
     * 函数内跳转，block重命名
     *
     * @param stmt jump
     */
    @Override
    public void visit(Jump stmt) {
        if (stmt.phiLabel != null) {
            PhysicalRegister a0 = registerMap.getReg("a0");
            Entity ans = currentIRFunc.phiMap.get(stmt.index + stmt.phiLabel);
            VirtualRegister des;
            if (!toReg.containsKey(String.valueOf(stmt.index))) {
                if (ans.type instanceof IntType intType && intType.typeName.equals(IntType.TypeName.BOOL)) {
                    des = newVirtualReg(true);
                } else {
                    des = newVirtualReg();
                }
                toReg.put(String.valueOf(stmt.index), des);
            } else {
                des = toReg.get(String.valueOf(stmt.index));
            }
            entity2value(ans, a0);
            storeVirtualRegister(a0, des);
        }
        currentBlock.pushBack(
                new JumpInst(renameBlock(stmt.targetName))
        );
    }

    /**
     * 每个函数仅有一个Return
     * 在回收栈之后直接在函数访问中添加
     */
    @Override
    public void visit(Return stmt) {
    }

    /**
     * ir中将i8转变为i1
     * asm中将entity名对应到同一个virtual register
     * TODO:constant
     *
     * @param stmt trunc
     */
    @Override
    public void visit(Trunc stmt) {
        toReg.put(stmt.result.toString(), getVirtualRegister(stmt.value));
    }

    /**
     * ir中将i1转变为i8
     * asm中将entity名对应到同一个virtual register
     *
     * @param stmt zero ext
     */
    @Override
    public void visit(Zext stmt) {
        toReg.put(stmt.result.toString(), getVirtualRegister(stmt.value));
    }

    /**
     * Phi
     * 在栈上为每一组phi分配一个空间
     * branch、jump在跳转前由phiMap中取值存到这个空间
     * 后进入的子跳转语句可以覆盖前面的
     *
     * @param stmt phi
     */
    @Override
    public void visit(Phi stmt) {
        PhysicalRegister a0 = registerMap.getReg("a0");
        loadVirtualRegister(a0, toReg.get(String.valueOf(stmt.phiLabel)));
        storeRegister(a0, stmt.result);
    }

}
