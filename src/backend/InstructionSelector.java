package backend;

import asm.*;
import asm.instruction.*;
import asm.operand.*;
import asm.section.*;
import ir.*;
import ir.entity.Entity;
import ir.entity.Storage;
import ir.entity.constant.*;
import ir.entity.var.*;
import ir.function.*;
import ir.irType.*;
import ir.stmt.instruction.*;
import ir.stmt.terminal.*;
import utility.Pair;
import utility.error.InternalException;

import java.util.*;

/**
 * @author F
 * 遍历IR，转化为ASM指令
 */
public class InstructionSelector implements IRVisitor {
    //使用的所用物理寄存器
    PhysicalRegMap registerMap = new PhysicalRegMap();
    Program program;

    //所有的virtual register
    HashMap<String, VirtualRegister> toReg;

    Func currentFunc;
    Function currentIRFunc;
    Block currentBlock;

    int maxParamCnt = 0;

    Imm zero = new Imm(0);

    //共用的重命名规则
    private String renameBlock(String blockName) {
        return ".L" + blockName + "_" + currentFunc.name;
    }

    private Operand toOperand(Entity entity) {
        if (entity instanceof GlobalVar globalVar) {
            VirtualRegister globalVarReg;
            if (toReg.containsKey(entity.toString())) {
                globalVarReg = toReg.get(entity.toString());
            } else {
                globalVarReg = newVirtualReg(false);
                toReg.put(entity.toString(), globalVarReg);
            }
            if (globalVar.storage instanceof ConstString) {//地址
                currentBlock.pushBack(
                        new GlobalAddrInst(globalVarReg, globalVar.identity)
                );
            } else { //全局变量地址载入
                PhysicalRegister t0 = registerMap.getReg("t0");
                t0.size = 4;
                currentBlock.pushBack(
                        new GlobalAddrInst(t0, globalVar.identity)
                );
                if (isBool(globalVar.storage.type)) {
                    globalVarReg.size = 1;
                } else {
                    globalVarReg.size = 4;
                }
                //全局变量的值
                currentBlock.pushBack(
                        new LoadInst(t0, globalVarReg, zero)
                );
            }
            return globalVarReg;
        } else if (entity instanceof Constant constant) {
            return const2operand(constant);
        } else {
            return getVirtualRegister(entity);
        }
    }

    /**
     * 找到对应的virtual register
     * IR上entity映射到asm的VirtualRegister
     *
     * @param entity IR entity
     * @return register
     */
    public VirtualRegister getVirtualRegister(Entity entity) {
        String name = entity.toString();
        IRType type;
        if (entity instanceof LocalVar ptr) {
            type = ptr.storage.type;
        } else if (entity instanceof LocalTmpVar tmpVar) {
            type = tmpVar.type;
        } else {
            throw new InternalException("get virtual register of unexpected entity " + entity);
        }
        boolean flag = isBool(type);
        if (toReg.containsKey(name)) {
            return toReg.get(name);
        }
        VirtualRegister register = newVirtualReg(flag);
        toReg.put(name, register);
        return register;
    }

    private VirtualRegister newVirtualReg(boolean isBool) {
        if (isBool) {
            return new VirtualRegister(1);
        } else {
            return new VirtualRegister(4);
        }
    }

    private boolean isBool(IRType type) {
        return type instanceof IntType intType &&
                (intType.typeName.equals(IntType.TypeName.BOOL)
                        || intType.typeName.equals(IntType.TypeName.TMP_BOOL));
    }

    private void const2value(Constant constant, PhysicalRegister reg) {
        Operand operand = const2operand(constant);
        if (operand instanceof Imm) {
            currentBlock.pushBack(
                    new LiInst(reg, (Imm) operand)
            );
        } else {
            currentBlock.pushBack(
                    new MoveInst(reg, (PhysicalRegister) operand)
            );
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
        } else if (constant instanceof Null) {
            return registerMap.getReg("zero");
        } else {
            throw new InternalException("can not resolve constant");
        }
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
            PhysicalRegister t0 = registerMap.getReg("t0");
            t0.size = 4;
            currentBlock.pushBack(
                    new LuiInst(t0, new Imm((num >> 12)))
            );
            if ((num & 0xFFF) != 0) {
                currentBlock.pushBack(
                        new ImmBinaryInst(
                                t0,
                                new Imm(num & 0xFFF),
                                t0,
                                ImmBinaryInst.Opcode.addi
                        )
                );
            }
            return t0;
        }
    }

    public InstructionSelector(Program program) {
        this.program = program;
    }

    /**
     * IRRoot
     * 访问全局变量的定义（Global指令转为全局变量表示）
     * 初始化函数
     * 一一访问函数
     *
     * @param root Mx的根
     */
    @Override
    public void visit(IRRoot root) {
        //全局变量、字符串常量
        root.globalVarDefBlock.statements.forEach(
                stmt -> stmt.accept(this)
        );
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
        maxParamCnt = 0;
        toReg = new HashMap<>();
        //顺序访问每一个block
        for (Map.Entry<String, BasicBlock> entry : function.blockMap.entrySet()) {
            BasicBlock block = entry.getValue();
            currentBlock = new Block(renameBlock(block.label));
            currentFunc.funcBlocks.add(currentBlock);
            visit(block);
        }
        currentFunc.extraParamCnt = (maxParamCnt > 8 ? maxParamCnt - 8 : 0);
        //入参（相当于局部变量）
        PhysicalRegister sp = registerMap.getReg("sp");
        PhysicalRegister fp = registerMap.getReg("fp");
        //访问函数alloca开好了virtual reg，将值传入（store）
        int i;
        Register paramReg;
        PhysicalRegister reg;
        for (i = 0; i < 8; ++i) {
            if (i == function.parameterList.size()) {
                break;
            }
            reg = registerMap.getReg("a" + i);
            paramReg = getVirtualRegister(function.parameterList.get(i));
            currentBlock.pushBack(
                    new MoveInst(paramReg, reg)
            );
        }
        PhysicalRegister t2 = registerMap.getReg("t2");
        for (; i < function.parameterList.size(); ++i) {
            paramReg = getVirtualRegister(function.parameterList.get(i));
            currentBlock.pushBack(
                    new LoadInst(fp, paramReg, new Imm((i - 8) << 2))
            );
        }
        //最后一个块
        currentBlock = currentFunc.funcBlocks.get(currentFunc.funcBlocks.size() - 1);
        //有返回值，返回值放在a0
        if (!(function.retType instanceof VoidType)) {
            PhysicalRegister a0 = registerMap.getReg("a0");
            currentBlock.pushBack(
                    new MoveInst(a0, getVirtualRegister(function.retVal))
            );
        }
        currentBlock.pushBack(
                new RetInst()
        );
    }

    @Override
    public void visit(BasicBlock basicBlock) {
        //访问block内的每一个语句
        basicBlock.statements.forEach(
                stmt -> stmt.accept(this)
        );
        //单独访问terminal
        basicBlock.tailStmt.accept(this);
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
     * Binary
     *
     * @param stmt 二元运算
     */
    @Override
    public void visit(Binary stmt) {
        VirtualRegister resultReg = getVirtualRegister(stmt.result);
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
                currentBlock.pushBack(
                        new LiInst(resultReg, imm)
                );
            }
            //寄存器赋值
            else {
                currentBlock.pushBack(
                        new MoveInst(resultReg, (PhysicalRegister) operand)
                );
            }
            return;
        }
        Operand operand1 = toOperand(stmt.op1), operand2 = toOperand(stmt.op2);
        PhysicalRegister t1 = registerMap.getReg("t1");
        t1.size = 4;//暂存整数
        if (operand1 instanceof Imm imm) {
            if (stmt.operator.equals(Binary.Operator.sub) ||
                    stmt.operator.equals(Binary.Operator.mul) ||
                    stmt.operator.equals(Binary.Operator.sdiv) ||
                    stmt.operator.equals(Binary.Operator.srem)) {
                currentBlock.pushBack(
                        new LiInst(t1, imm)
                );
                currentBlock.pushBack(
                        new BinaryInst(t1, (Register) operand2, resultReg, stmt.operator)
                );
            } else {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) operand2, imm, resultReg, stmt.operator)
                );
            }
            return;
        } else if (operand2 instanceof Imm imm) {
            if (stmt.operator.equals(Binary.Operator.sub)) {
                imm = new Imm(-imm.value);
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) operand1, imm, resultReg, ImmBinaryInst.Opcode.addi)
                );
            }
            if (stmt.operator.equals(Binary.Operator.mul) ||
                    stmt.operator.equals(Binary.Operator.sdiv) ||
                    stmt.operator.equals(Binary.Operator.srem)) {
                currentBlock.pushBack(
                        new LiInst(t1, imm)
                );
                currentBlock.pushBack(
                        new BinaryInst((Register) operand1, t1, resultReg, stmt.operator)
                );
            } else {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) operand1, imm, resultReg, stmt.operator)
                );
            }
            return;
        } else {
            currentBlock.pushBack(
                    new BinaryInst((Register) operand1, (Register) operand2, resultReg, stmt.operator)
            );
        }
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
        PhysicalRegister reg;
        Operand param;
        if (stmt.parameterList.size() <= 8) {
            for (int i = 0; i < stmt.parameterList.size(); ++i) {
                reg = registerMap.getReg("a" + i);
                param = toOperand(stmt.parameterList.get(i));
                if (param instanceof Imm) {
                    currentBlock.pushBack(
                            new LiInst(reg, (Imm) param)
                    );
                } else {
                    currentBlock.pushBack(
                            new MoveInst(reg, (Register) param)
                    );
                }
            }
        } else {
            int i;
            for (i = 0; i < 8; ++i) {
                reg = registerMap.getReg("a" + i);
                param = toOperand(stmt.parameterList.get(i));
                if (param instanceof Imm) {
                    currentBlock.pushBack(
                            new LiInst(reg, (Imm) param)
                    );
                } else {
                    currentBlock.pushBack(
                            new MoveInst(reg, (Register) param)
                    );
                }
            }
            PhysicalRegister sp = registerMap.getReg("sp");
            for (; i < stmt.parameterList.size(); ++i) {
                param = toOperand(stmt.parameterList.get(i));
                currentBlock.pushBack(
                        new StoreInst(param, sp, new Imm((i << 2)))
                );
            }
        }
        //函数调用
        currentBlock.pushBack(
                new CallInst(stmt.function.funcName)
        );
        //存返回值
        if (!(stmt.function.retType instanceof VoidType)) {
            currentBlock.pushBack(
                    new MoveInst(getVirtualRegister(stmt.result), registerMap.getReg("a0"))
            );
        }
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
        Operand ptr = toOperand(stmt.ptrVal);
        Operand idx = toOperand(stmt.idx);
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
        if (idx instanceof Imm imm) {
            if (baseSize == 4) {
                imm.value <<= 2;
            }
            currentBlock.pushBack(
                    new ImmBinaryInst((Register) ptr, imm, getVirtualRegister(stmt.result), Binary.Operator.add)
            );
        } else {
            if (baseSize == 4) {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) idx, new Imm(2), (Register) idx, ImmBinaryInst.Opcode.slli)
                );
            }
            //计算地址
            currentBlock.pushBack(
                    new BinaryInst((Register) ptr, (Register) idx, getVirtualRegister(stmt.result), BinaryInst.Opcode.add)
            );
        }
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
        VirtualRegister resultReg = getVirtualRegister(stmt.result);
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
            currentBlock.pushBack(
                    new LiInst(resultReg, new Imm(ans))
            );
            return;
        }
        //其余
        Operand op1 = toOperand(stmt.op1);
        Operand op2 = toOperand(stmt.op2);
        if (!(stmt.cond.equals(Icmp.Cond.eq) || stmt.cond.equals(Icmp.Cond.ne))) {
            if (stmt.cond.equals(Icmp.Cond.sle)) {
                currentBlock.pushBack(
                        new CmpInst(op1, op2, resultReg, Icmp.Cond.sgt)
                );
                currentBlock.pushBack(
                        new NotInst(resultReg, resultReg)
                );
            } else if (stmt.cond.equals(Icmp.Cond.sge)) {
                currentBlock.pushBack(
                        new CmpInst(op1, op2, resultReg, Icmp.Cond.slt)
                );
                currentBlock.pushBack(
                        new NotInst(resultReg, resultReg)
                );
            } else {
                currentBlock.pushBack(
                        new CmpInst(op1, op2, resultReg, stmt.cond)
                );
            }
        } else {
            if (op1 instanceof Imm) {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) op2, (Imm) op1, resultReg, Binary.Operator.sub)
                );
            } else if (op2 instanceof Imm) {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) op1, (Imm) op2, resultReg, Binary.Operator.sub)
                );
            } else {
                currentBlock.pushBack(
                        new BinaryInst((Register) op1, (Register) op2, resultReg, BinaryInst.Opcode.sub)
                );
            }
            currentBlock.pushBack(
                    new EqualZeroInst(resultReg, resultReg, stmt.cond)
            );
        }
    }


    /**
     * Load
     * %3 = load i32, ptr %1
     *
     * @param stmt load
     */
    @Override
    public void visit(Load stmt) {
        currentBlock.pushBack(
                new LoadInst((Register) toOperand(stmt.result),
                        (Register) toOperand(stmt.pointer),
                        zero)
        );
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
        currentBlock.pushBack(
                new StoreInst(toOperand(stmt.value),
                        (Register) toOperand(stmt.pointer),
                        zero)
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
        //phi
        if (stmt.phiLabel != null) {
            Entity ans = currentIRFunc.phiMap.get(stmt.index + stmt.phiLabel);
            Operand ansOperand = toOperand(ans);
            if (ansOperand instanceof Imm imm) {
                currentBlock.pushBack(
                        new LiInst(
                                getVirtualRegister(currentIRFunc.phiResult.get(stmt.index)),
                                imm
                        ));
            } else {
                currentBlock.pushBack(
                        new MoveInst(
                                getVirtualRegister(currentIRFunc.phiResult.get(stmt.index)),
                                (Register) ansOperand
                        ));
            }
        }
        Operand condReg = toOperand(stmt.condition);
        if (condReg instanceof Imm imm) {
            if (imm.value == 1) {//true
                currentBlock.pushBack(
                        new JumpInst(renameBlock(stmt.trueBranch.label))
                );
            } else {
                currentBlock.pushBack(
                        new JumpInst(renameBlock(stmt.falseBranch.label))
                );
            }

        } else if (condReg instanceof Register register) {
            currentBlock.pushBack(
                    new ImmBinaryInst(register, new Imm(1), register, ImmBinaryInst.Opcode.andi)
            );
            //neqz branch
            currentBlock.pushBack(
                    new BranchInst(register, renameBlock(stmt.trueBranch.label))
            );
            currentBlock.pushBack(
                    new JumpInst(renameBlock(stmt.falseBranch.label))
            );
        }
    }

    /**
     * Jump
     * 函数内跳转，block重命名
     *
     * @param stmt jump
     */
    @Override
    public void visit(Jump stmt) {
        if (stmt.phiLabel != null
                && currentIRFunc.phiMap.containsKey(stmt.index + stmt.phiLabel)) {
            Entity ans = currentIRFunc.phiMap.get(stmt.index + stmt.phiLabel);
            Operand ansOperand = toOperand(ans);
            if (ansOperand instanceof Imm imm) {
                currentBlock.pushBack(
                        new LiInst(
                                getVirtualRegister(currentIRFunc.phiResult.get(stmt.index)),
                                imm
                        ));
            } else {
                currentBlock.pushBack(
                        new MoveInst(
                                getVirtualRegister(currentIRFunc.phiResult.get(stmt.index)),
                                (Register) ansOperand
                        ));
            }
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
     *
     * @param stmt trunc
     */
    @Override
    public void visit(Trunc stmt) {
        currentBlock.pushBack(
                new MoveInst(getVirtualRegister(stmt.result), getVirtualRegister(stmt.value))
        );
    }

    /**
     * ir中将i1转变为i8
     *
     * @param stmt zero ext
     */
    @Override
    public void visit(Zext stmt) {
        VirtualRegister resultReg = getVirtualRegister(stmt.result);
        if (stmt.value instanceof ConstBool bool) {
            if (bool.value) {
                currentBlock.pushBack(
                        new LiInst(resultReg, new Imm(1))
                );
            } else {
                currentBlock.pushBack(
                        new LiInst(resultReg, zero)
                );
            }
        } else {
            currentBlock.pushBack(
                    new MoveInst(resultReg, getVirtualRegister(stmt.value))
            );
        }
    }

    /**
     * Phi
     * branch、jump在跳转前由phiMap中取值存到result
     * 后进入的子跳转语句可以覆盖前面的
     *
     * @param stmt phi
     */
    @Override
    public void visit(Phi stmt) {
    }

}