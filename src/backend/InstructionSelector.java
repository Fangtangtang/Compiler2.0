package backend;

import asm.*;
import asm.instruction.*;
import asm.operand.*;
import asm.section.*;
import ir.*;
import ir.entity.Entity;
import ir.entity.SSAEntity;
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
    public PhysicalRegMap registerMap = new PhysicalRegMap();
    PhysicalRegister t0, t1, t2, t3;

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

    private Operand toOperand(SSAEntity ssaEntity) {
        Entity entity = ssaEntity.origin;
        if (entity instanceof GlobalVar globalVar) {
            //全局变量在虚拟寄存器的副本（string的地址，其余的值）
            //localTmpVar
            VirtualRegister globalVarReg;
            if (toReg.containsKey(entity.toString())) {
                globalVarReg = toReg.get(entity.toString());
            } else {
                globalVarReg = newVirtualReg(entity.toString(), false);
                toReg.put(entity.toString(), globalVarReg);
            }
            if (globalVar.storage instanceof ConstString) {//地址
                currentBlock.pushBack(
                        new GlobalAddrInst(globalVarReg, globalVar.identity)
                );
            } else {
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
                        new LoadInst(t0, globalVarReg, zero, false, false, false)
                );
            }
            return globalVarReg;
        } else if (entity instanceof Constant constant) {
            return const2operand(constant);
        } else {
            return getVirtualRegister(ssaEntity);
        }
    }

    public VirtualRegister getVirtualRegister(SSAEntity ssaEntity) {
        Entity entity = ssaEntity.origin;
        String name;
        if (ssaEntity.lr == null) {
            name = entity.toString();
        } else {
            name = ssaEntity.lr.setName();
        }
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
        VirtualRegister register = newVirtualReg(name, flag);
        toReg.put(name, register);
        return register;
    }

    private VirtualRegister newVirtualReg(String name, boolean isBool) {
        if (isBool) {
            return new VirtualRegister(name, 1);
        } else {
            return new VirtualRegister(name, 4);
        }
    }

    private boolean isBool(IRType type) {
        return type instanceof IntType intType &&
                (intType.typeName.equals(IntType.TypeName.BOOL)
                        || intType.typeName.equals(IntType.TypeName.TMP_BOOL));
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
            return new Imm(constBool.value);
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

//    /**
//     * 指针对象
//     * - 全局变量：直接找到地址
//     * - 局部变量：为指向空间的指针，目标获得空间地址，此时未知
//     * - 局部临时变量：值为地址，只需取值
//     *
//     * @param reg    存地址的reg
//     * @param entity 指针对象
//     * @return pair：true 要地址，false已知或要值
//     */
//    private Pair<Register, Boolean> getPointedAddr(PhysicalRegister reg, Entity entity) {
//        if (entity instanceof GlobalVar globalVar) {
//            currentBlock.pushBack(
//                    new GlobalAddrInst(reg, globalVar.identity)
//            );
//            return new Pair<>(reg, false);
//        } else if (entity instanceof LocalVar localVar) {
//            return new Pair<>(getVirtualRegister(entity), true);
//        }
//        //包含值的virtual reg
//        return new Pair<>(getVirtualRegister(entity), false);
//    }

    private Pair<Register, Boolean> getPointedAddr(PhysicalRegister reg, SSAEntity ssaEntity) {
        Entity entity = ssaEntity.origin;
        if (entity instanceof GlobalVar globalVar) {
            currentBlock.pushBack(
                    new GlobalAddrInst(reg, globalVar.identity)
            );
            return new Pair<>(reg, false);
        } else if (entity instanceof LocalVar localVar) {
            return new Pair<>(getVirtualRegister(ssaEntity), true);
        }
        //包含值的virtual reg
        return new Pair<>(getVirtualRegister(ssaEntity), false);
    }


    public InstructionSelector(Program program) {
        this.program = program;
        t0 = registerMap.getReg("t0");
        t1 = registerMap.getReg("t1");
        t2 = registerMap.getReg("t2");
        t3 = registerMap.getReg("t3");
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

    private void setPhysicalRegSize(PhysicalRegister reg, Entity entity) {
        if (isBool(entity.type)) {
            reg.size = 1;
        } else {
            reg.size = 4;
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
        ArrayList<ASMInstruction> getParams = new ArrayList<>();
        SSAEntity param;
        PhysicalRegister reg;
        for (i = 0; i < 8; ++i) {
            if (i == function.parameterList.size()) {
                break;
            }
            reg = registerMap.getReg("a" + i);
            param = function.ssaParameterList.get(i);
            setPhysicalRegSize(reg, param.origin);
            paramReg = getVirtualRegister(param);
            getParams.add(
                    new MoveInst(paramReg, reg)
            );
        }
        for (; i < function.parameterList.size(); ++i) {
            paramReg = getVirtualRegister(function.ssaParameterList.get(i));
            //载入临时寄存器
            getParams.add(
                    new LoadInst(fp, t0, new Imm((i - 8) << 2))
            );
            getParams.add(
                    new MoveInst(paramReg, t0)
            );
        }
        currentFunc.funcBlocks.get(0).instructions.addAll(0, getParams);
        //最后一个块
        if (function.ret != null) {
            currentBlock = new Block(renameBlock(function.ret.label));
            currentFunc.funcBlocks.add(currentBlock);
        } else {
            currentBlock = currentFunc.funcBlocks.get(currentFunc.funcBlocks.size() - 1);
        }
        //有返回值，返回值放在a0
        if (!(function.retType instanceof VoidType)) {
            PhysicalRegister a0 = registerMap.getReg("a0");
            setPhysicalRegSize(a0, function.retVal);
            currentBlock.pushBack(
                    new MoveInst(a0, getVirtualRegister(function.ssaRetVal))
            );
        }
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
        getVirtualRegister(stmt.ssaResult);
    }


    /**
     * Binary
     *
     * @param stmt 二元运算
     */
    @Override
    public void visit(Binary stmt) {
        VirtualRegister resultReg = getVirtualRegister(stmt.ssaResult);
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
        Operand operand1 = toOperand(stmt.ssaOp1), operand2 = toOperand(stmt.ssaOp2);
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
                return;
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
        maxParamCnt = Math.max(stmt.ssaParameterList.size(), maxParamCnt);
        //入参
        //若参数<=8，直接使用寄存器a0-a7传递
        PhysicalRegister reg;
        SSAEntity parameter;
        Operand param;
        if (stmt.ssaParameterList.size() <= 8) {
            for (int i = 0; i < stmt.ssaParameterList.size(); ++i) {
                reg = registerMap.getReg("a" + i);
                parameter = stmt.ssaParameterList.get(i);
                setPhysicalRegSize(reg, parameter.origin);
                param = toOperand(parameter);
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
                parameter = stmt.ssaParameterList.get(i);
                setPhysicalRegSize(reg, parameter.origin);
                param = toOperand(parameter);
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
            for (; i < stmt.ssaParameterList.size(); ++i) {
                param = toOperand(stmt.ssaParameterList.get(i));
                if (param instanceof Imm) {
                    currentBlock.pushBack(
                            new LiInst(t1, (Imm) param)
                    );
                    param = t1;
                } else if (param instanceof PhysicalRegister) {
                    param = ((PhysicalRegister) param);
                }
                //VirtualReg
                //TODO:简化
                else {
                    currentBlock.pushBack(
                            new MoveInst(t1, (Register) param)
                    );
                    param = t1;
                }
                currentBlock.pushBack(
                        new StoreInst(param, sp, new Imm(((i - 8) << 2)))
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
                    new MoveInst(getVirtualRegister(stmt.ssaResult), registerMap.getReg("a0"))
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
        Operand ptr = toOperand(stmt.ssaPtrVal);
        Operand idx = toOperand(stmt.ssaIdx);
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
                    new ImmBinaryInst((Register) ptr, imm, getVirtualRegister(stmt.ssaResult), Binary.Operator.add)
            );
        } else {
            if (baseSize == 4) {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) idx, new Imm(2), (Register) idx, ImmBinaryInst.Opcode.slli)
                );
            }
            //计算地址
            currentBlock.pushBack(
                    new BinaryInst((Register) ptr, (Register) idx, getVirtualRegister(stmt.ssaResult), BinaryInst.Opcode.add)
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
        VirtualRegister resultReg = getVirtualRegister(stmt.ssaResult);
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
        Operand op1 = toOperand(stmt.ssaOp1);
        Operand op2 = toOperand(stmt.ssaOp2);
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
            if (op1 instanceof Imm imm) {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) op2, new Imm(-imm.value), t1, ImmBinaryInst.Opcode.addi)
                );
            } else if (op2 instanceof Imm imm) {
                currentBlock.pushBack(
                        new ImmBinaryInst((Register) op1, new Imm(-imm.value), t1, ImmBinaryInst.Opcode.addi)
                );
            } else {
                currentBlock.pushBack(
                        new BinaryInst((Register) op1, (Register) op2, t1, BinaryInst.Opcode.sub)
                );
            }
            currentBlock.pushBack(
                    new EqualZeroInst(t1, resultReg, stmt.cond)
            );
        }
    }


    /**
     * Load
     * %3 = load i32, ptr %1
     * result is localVar\ptr(localTmpPtr)
     * ptr -> ptr
     *
     * @param stmt load
     */
    @Override
    public void visit(Load stmt) {
        //取数到t2
        Pair<Register, Boolean> pointerPair = getPointedAddr(t1, stmt.ssaPtr);
        PhysicalRegister rs1Reg;
        //global
        if (pointerPair.getFirst() instanceof PhysicalRegister register) {
            currentBlock.pushBack(
                    new LoadInst(register, t2, zero)
            );
        }
        //localVar
        else if (pointerPair.getSecond()) {
            currentBlock.pushBack(
                    new MoveInst(t2, pointerPair.getFirst())
            );
        }
        //localTmpVar
        else {
            currentBlock.pushBack(
                    new LoadInst(pointerPair.getFirst(), t2, zero)
            );
        }
        //存数
        Pair<Register, Boolean> resultPair = getPointedAddr(t1, stmt.ssaResult);
        PhysicalRegister addr;
        if (resultPair.getFirst() instanceof PhysicalRegister) {
            addr = (PhysicalRegister) resultPair.getFirst();
            currentBlock.pushBack(
                    new StoreInst(t2, addr, zero)
            );
        } else {
            currentBlock.pushBack(
                    new MoveInst(resultPair.getFirst(), t2)
            );
        }
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
     * Global:取地址
     * LocalVar:move
     * LocalTmpVar：取值
     *
     * @param stmt store
     */
    @Override
    public void visit(Store stmt) {
        Operand value = toOperand(stmt.ssaValue);
        Pair<Register, Boolean> pair = getPointedAddr(t1, stmt.ssaPtr);
        //LocalVar
        if (pair.getSecond()) {
            if (value instanceof Imm) {
                currentBlock.pushBack(
                        new LiInst(pair.getFirst(), (Imm) value)
                );
            } else {
                currentBlock.pushBack(
                        new MoveInst(pair.getFirst(), (Register) value)
                );
            }
            return;
        }
        PhysicalRegister addr;
        if (pair.getFirst() instanceof PhysicalRegister register) {
            addr = register;
        } else {
            currentBlock.pushBack(
                    new MoveInst(t2, pair.getFirst())
            );
            addr = t2;
        }
        if (value instanceof Imm) {
            currentBlock.pushBack(
                    new LiInst(t3, (Imm) value)
            );
            currentBlock.pushBack(
                    new StoreInst(t3, addr, zero)
            );
        } else {
            currentBlock.pushBack(
                    new StoreInst(value, addr, zero)
            );
        }
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
            SSAEntity ans = stmt.ssaResult;
            Operand ansOperand = toOperand(ans);
            if (ansOperand instanceof Imm imm) {
                currentBlock.pushBack(
                        new LiInst(
                                getVirtualRegister(currentIRFunc.ssaPhiResult.get(stmt.index)),
                                imm
                        ));
            } else {
                currentBlock.pushBack(
                        new MoveInst(
                                getVirtualRegister(currentIRFunc.ssaParameterList.get(stmt.index)),
                                (Register) ansOperand
                        ));
            }
        }
        Operand condReg = toOperand(stmt.ssaCondition);
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
                    new ImmBinaryInst(register, new Imm(true), register, ImmBinaryInst.Opcode.andi)
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
                && currentIRFunc.phiResult.containsKey(stmt.index)) {
            SSAEntity ans = stmt.ssaResult;
            Operand ansOperand = toOperand(ans);
            if (ansOperand instanceof Imm imm) {
                currentBlock.pushBack(
                        new LiInst(
                                getVirtualRegister(currentIRFunc.ssaPhiResult.get(stmt.index)),
                                imm
                        ));
            } else {
                currentBlock.pushBack(
                        new MoveInst(
                                getVirtualRegister(currentIRFunc.ssaPhiResult.get(stmt.index)),
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
                new MoveInst(getVirtualRegister(stmt.ssaResult), getVirtualRegister(stmt.ssaValue))
        );
    }

    /**
     * ir中将i1转变为i8
     *
     * @param stmt zero ext
     */
    @Override
    public void visit(Zext stmt) {
        VirtualRegister resultReg = getVirtualRegister(stmt.ssaResult);
        if (stmt.value instanceof ConstBool bool) {
            currentBlock.pushBack(
                    new LiInst(resultReg, new Imm(bool.value))
            );
        } else {
            currentBlock.pushBack(
                    new MoveInst(resultReg, getVirtualRegister(stmt.ssaValue))
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