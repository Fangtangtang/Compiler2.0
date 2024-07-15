package backend.optimizer;

import asm.*;
import asm.instruction.*;
import asm.operand.*;
import backend.optimizer.interferenceGraph.*;
import utility.Counter;
import utility.Pair;
import utility.error.InternalException;

import java.util.*;

/**
 * @author F
 * 作用域asm的函数
 * 图染色寄存器分配
 */
public class GraphColoring {
    Func func;

    Set<VirtualRegister> addedRegs;
    InterferenceGraph interferenceGraph;
    Stack<UncoloredNode> selectStack;
    NodeSet nodeSet;
    MoveInstSet moveInstSet;
    //reg到冲突图node的映射
    HashMap<Register, Node> reg2node;
    int K = 25;
    //    int K = 3;
    PhysicalRegMap registerMap;
    PhysicalRegister fp, sp, t0;
    HashMap<String, PrecoloredNode> precoloredNodeMap;

    public GraphColoring(Func func, PhysicalRegMap registerMap) {
        this.func = func;
        this.registerMap = registerMap;
        this.addedRegs = new HashSet<>();
        fp = registerMap.getReg("fp");
        sp = registerMap.getReg("sp");
        t0 = registerMap.getReg("t0");
    }

    public void execute() {
        while (true) {
            clear();
            LivenessAnalysis analyzer = new LivenessAnalysis(func);
            analyzer.analysisLiveOut();
            buildInterferenceGraph();
//            this.interferenceGraph.print();
            makeWorkList();
            //repeat until workLists are empty
            while (true) {
                if (!nodeSet.simplifyWorkList.isEmpty()) {
                    simplify();
                } else if (!moveInstSet.workListMoves.isEmpty()) {
                    coalesce();
                } else if (!nodeSet.freezeWorkList.isEmpty()) {
                    freeze();
                } else if (!nodeSet.spillWorkList.isEmpty()) {
                    selectSpill();
                } else {
                    break;
                }
            }
            //try to assign color
            assignColor();
            if (nodeSet.spilledNodes.isEmpty()) {
                break;
            }
            rewriteFunction();
        }
        assignColorToReg();
    }

    /**
     * execute循环执行前的初始化
     * 将所有更新结果清空
     */
    void clear() {
        interferenceGraph = new InterferenceGraph();
        selectStack = new Stack<>();
        nodeSet = new NodeSet();
        moveInstSet = new MoveInstSet();
        reg2node = new HashMap<>();
        Node.id = 0;
        precoloredNodeMap = new HashMap<>();
    }

    Node toNode(Register register) {
        if (reg2node.containsKey(register)) {
            return reg2node.get(register);
        }
        if (register instanceof PhysicalRegister physicalRegister) {
            PrecoloredNode node;
            if (precoloredNodeMap.containsKey(physicalRegister.name)) {
                node = precoloredNodeMap.get(physicalRegister.name);
            } else {
                PhysicalRegister reg = registerMap.getReg(physicalRegister.name);
                node = new PrecoloredNode((PhysicalRegister) register);
                precoloredNodeMap.put(reg.name, node);
            }
            reg2node.put(physicalRegister, node);
            return node;
        }
        if (register instanceof VirtualRegister virtualReg) {
            UncoloredNode node = new UncoloredNode(virtualReg);
            reg2node.put(virtualReg, node);
            interferenceGraph.adjList.put(node, new HashSet<>());
            return node;
        }
        throw new InternalException("unexpected register type");
    }

    /**
     * 由函数构建冲突图
     * Register转为Node
     */
    void buildInterferenceGraph() {
        Block block;
        ASMInstruction instruction;
        HashSet<Register> live;
        ArrayList<Register> use;
        Register def;
        for (int i = 0; i < func.funcBlocks.size(); i++) {
            block = func.funcBlocks.get(i);
            live = new HashSet<>(block.liveOut);//live reg after current inst
            //visit inst in reverse order
            ListIterator<ASMInstruction> iter = block.instructions.listIterator(
                    block.instructions.size()
            );
            while (iter.hasPrevious()) {
                instruction = iter.previous();
                use = instruction.getUse();
                def = instruction.getDef();
                if (instruction instanceof MoveInst moveInst) {
                    //live \ use :避免后续在mv间加实边
                    use.forEach(live::remove);
                    for (var useReg : use) {
                        toNode(useReg).moveList.add(moveInst);
                    }
                    toNode(def).moveList.add(moveInst);
                    moveInstSet.workListMoves.add(moveInst);
                }
                //添加实冲突边
                if (def != null) {
                    live.add(def);
                    Node defNode = toNode(def);
                    for (var liveReg : live) {
                        interferenceGraph.addEdge(toNode(liveReg), defNode);
                    }
                    live.remove(def);
                }
                if (use != null) {
                    live.addAll(use);
                }
            }
        }
    }

    boolean isMoveRelated(Node node) {
        return !node.moveList.isEmpty();
    }

    void makeWorkList() {
        //precolored/initial
        for (Node node : reg2node.values()) {
            if (node instanceof PrecoloredNode precoloredNode) {
                nodeSet.precolored.add(precoloredNode);
            } else {
                nodeSet.initial.add((UncoloredNode) node);
            }
        }
        //classify nodes in initial to 3 classes
        for (UncoloredNode node : nodeSet.initial) {
            if (node.degree >= K) {
                nodeSet.spillWorkList.add(node);
            } else if (isMoveRelated(node)) {
                nodeSet.freezeWorkList.add(node);
            } else {
                nodeSet.simplifyWorkList.add(node);
            }
        }
    }

    void simplify() {
        UncoloredNode node = nodeSet.simplifyWorkList.iterator().next();
        nodeSet.simplifyWorkList.remove(node);
        selectStack.push(node);
        for (Node adjNode : adjacent(node)) {
            if (adjNode instanceof UncoloredNode uncoloredNode) {
                decrementDegree(uncoloredNode);
            }
        }
    }

    HashSet<Node> adjacent(Node node) {
        HashSet<Node> ret = new HashSet<>(interferenceGraph.getAdjList(node));
        for (UncoloredNode element : selectStack) {
            ret.remove(element);
        }
        ret.removeAll(nodeSet.coalescedNodes);
        return ret;
    }

    void decrementDegree(UncoloredNode m) {
        int d = m.degree;
        m.degree -= 1;
        if (d == K) {
            //自身可能可以合并
            enableMove(m);
            //邻结点可能可以合并
            for (Node node : adjacent(m)) {
                enableMove(node);
            }
            nodeSet.spillWorkList.remove(m);
            //成为低度数移动相关
            if (moveRelated(m)) {
                nodeSet.freezeWorkList.add(m);
            }
            //成为低度数移动无关
            else {
                nodeSet.simplifyWorkList.add(m);
            }
        }
    }

    //结点相关的仍然有可能被合并的move
    HashSet<MoveInst> nodeMoves(Node node) {
        HashSet<MoveInst> ret = new HashSet<>();
        for (MoveInst moveInst : node.moveList) {
            if (moveInstSet.workListMoves.contains(moveInst)
                    || moveInstSet.activeMoves.contains(moveInst)) {
                ret.add(moveInst);
            }
        }
        return ret;
    }

    boolean moveRelated(Node node) {
        return !nodeMoves(node).isEmpty();
    }

    //将可能的合并指令都标为 做好合并准备（不一定是真正做好合并准备）
    void enableMove(Node node) {
        for (MoveInst moveInst : nodeMoves(node)) {
            if (moveInstSet.activeMoves.contains(moveInst)) {
                moveInstSet.activeMoves.remove(moveInst);
                moveInstSet.workListMoves.add(moveInst);
            }
        }
    }

    void coalesce() {
        MoveInst moveInst = moveInstSet.workListMoves.iterator().next();
        Node x = getAlias(moveInst.rs1), y = getAlias(moveInst.rd);
        Node u, v;
        if (y instanceof PrecoloredNode) {
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        moveInstSet.workListMoves.remove(moveInst);
        //两结点被间接合并
        if (u.equals(v)) {
            moveInstSet.coalescedMoves.add(moveInst);
            addWorkList(u);
        }
        //两结点Precolored、两结点连边冲突
        else if (v instanceof PrecoloredNode
                || interferenceGraph.adjSet.contains(new Pair<>(u, v))
        ) {
            moveInstSet.constrainedMoves.add(moveInst);
            addWorkList(u);
            addWorkList(v);
        } else if ((u instanceof PrecoloredNode && validUnderGeorge(u, v))
                || u instanceof UncoloredNode && validUnderBriggs(u, v)) {
            moveInstSet.coalescedMoves.add(moveInst);
            combine(u, (UncoloredNode) v);
            addWorkList(u);
        } else {
            moveInstSet.activeMoves.add(moveInst);
        }
    }

    void combine(Node u, UncoloredNode v) {
        if (nodeSet.freezeWorkList.contains(v)) {
            nodeSet.freezeWorkList.remove(v);
        } else {
            nodeSet.spillWorkList.remove(v);
        }
        nodeSet.coalescedNodes.add(v);
        v.alias = u;
        u.moveList.addAll(v.moveList);
        enableMove(v);
        for (Node t : adjacent(v)) {
            interferenceGraph.addEdge(t, u);
            if (t instanceof UncoloredNode uncoloredNode) {
                decrementDegree(uncoloredNode);
            }
        }
        //低度数传递有关变为高度数
        if (u instanceof UncoloredNode &&
                u.degree >= K &&
                nodeSet.freezeWorkList.contains(u)) {
            nodeSet.freezeWorkList.remove(u);
            nodeSet.spillWorkList.add((UncoloredNode) u);
        }
    }

    //非预着色、移动无关、低度数
    //解冻，尝试简化
    void addWorkList(Node node) {
        if (!(node instanceof PrecoloredNode) &&
                !(moveRelated(node)) &&
                node.degree < K) {
            nodeSet.freezeWorkList.remove(node);
            nodeSet.simplifyWorkList.add((UncoloredNode) node);
        }
    }

    Node getAlias(Register register) {
        return alias(toNode(register));
    }

    Node alias(Node node) {
        if (nodeSet.coalescedNodes.contains(node)) {
            return alias(node.alias);
        }
        return node;
    }

    boolean validUnderGeorge(Node u, Node v) {
        for (Node t : adjacent(v)) {
            if (!george(t, u)) {
                return false;
            }
        }
        return true;
    }

    boolean george(Node t, Node r) {
        return t.degree < K ||//低度数结点，可以被简化
                //已经有冲突，合并后不增加
                t instanceof PrecoloredNode ||
                interferenceGraph.adjSet.contains(new Pair<>(t, r));
    }

    boolean validUnderBriggs(Node u, Node v) {
        HashSet<Node> nodes = new HashSet<>(adjacent(u));
        nodes.addAll(adjacent(v));
        int k = 0;
        for (Node n : nodes) {
            if (n.degree >= K) {
                ++k;
            }
        }
        return k < K;
    }

    //将node相关的move冻结
    //去掉与之相关度degree
    void freeze() {
        UncoloredNode node = nodeSet.freezeWorkList.iterator().next();
        nodeSet.freezeWorkList.remove(node);
        nodeSet.simplifyWorkList.add(node);
        freezeMoves(node);
    }

    void freezeMoves(UncoloredNode node) {
        Node v;
        Node aliasNode = alias(node);
        for (MoveInst moveInst : nodeMoves(node)) {
            //寻找原先与u有传递关系的
            Node x = getAlias(moveInst.rs1), y = getAlias(moveInst.rd);
            if (y.equals(aliasNode)) {
                v = x;
            } else {
                v = y;
            }
            //将这些指令冻结
            moveInstSet.activeMoves.remove(moveInst);
            moveInstSet.frozenMoves.add(moveInst);
            //修改workList
            if (v instanceof UncoloredNode && nodeMoves(v).isEmpty() && v.degree < K) {
                nodeSet.freezeWorkList.remove(v);
                nodeSet.simplifyWorkList.add((UncoloredNode) v);
            }
        }
    }

    //TODO:spill启发式
    //新加入的virtual代替原来spill不了的，无限死循环！！！
    //选择degree最大的
    void selectSpill() {
        int maxDegree = -1;
        UncoloredNode m = null;
        for (UncoloredNode node : nodeSet.spillWorkList) {
            if (!addedRegs.contains(node.register) &&
                    node.degree > maxDegree) {
                maxDegree = node.degree;
                m = node;
            }
        }
        if (m != null) {
            nodeSet.spillWorkList.remove(m);
            nodeSet.simplifyWorkList.add(m);
            freezeMoves(m);
        }
    }

    //selectStack中元素一一弹出，分配颜色
    void assignColor() {
        UncoloredNode node;
        HashSet<UncoloredNode> coloredNodes = new HashSet<>();
        Colors colors;
        while (!selectStack.empty()) {
            node = selectStack.pop();
            colors = new Colors();
            for (Node w : interferenceGraph.getAdjList(node)) {
                Node aliasNode = alias(w);
                if (aliasNode instanceof PrecoloredNode ||
                        coloredNodes.contains((UncoloredNode) aliasNode)) {
                    colors.available.remove(aliasNode.color);
                }
            }
            if (colors.available.isEmpty()) {
                nodeSet.spilledNodes.add(node);
            } else {
                coloredNodes.add(node);
                node.color = colors.available.iterator().next();
            }
        }
        //被合并结点的染色
        for (Node n : nodeSet.coalescedNodes) {
            n.color = alias(n).color;
        }
    }

    //counter计数，用于给use、def重命名
    HashMap<VirtualRegister, Pair<StackRegister, Counter>> spillToStack;
    LinkedList<ASMInstruction> rewrittenInstructions;
    Imm zero = new Imm(0);

    Pair<Register, Imm> getRegAddress(StackRegister register) {
        if (register.offset < (1 << 11)) {
            return new Pair<>(fp, new Imm(-register.offset));
        }
        t0.size = 4;
        //offset
        rewrittenInstructions.add(
                new LuiInst(t0, new Imm((register.offset >> 12)))
        );
        if ((register.offset & 0xFFF) != 0) {
            rewrittenInstructions.add(
                    new ImmBinaryInst(
                            t0,
                            new Imm(register.offset & 0xFFF),
                            t0,
                            ImmBinaryInst.Opcode.addi
                    )
            );
        }
        //计算真正地址
        rewrittenInstructions.add(
                new BinaryInst(fp, t0, t0, BinaryInst.Opcode.sub)
        );
        return new Pair<>(t0, zero);
    }

    //每句use前插入load
    VirtualRegister addLoad(VirtualRegister register) {
        Pair<StackRegister, Counter> pair = spillToStack.get(register);
        StackRegister stackRegister = pair.getFirst();
        Counter counter = pair.getSecond();
        ++counter.cnt;
        VirtualRegister newReg = new VirtualRegister(register.name + "." + counter.cnt, register.size);
        addedRegs.add(newReg);
        Pair<Register, Imm> addrPair = getRegAddress(stackRegister);
        rewrittenInstructions.add(
                new LoadInst(addrPair.getFirst(), newReg, addrPair.getSecond())
        );
        return newReg;
    }

    //每句def后插入store
    VirtualRegister addStore(VirtualRegister register) {
        Pair<StackRegister, Counter> pair = spillToStack.get(register);
        StackRegister stackRegister = pair.getFirst();
        Counter counter = pair.getSecond();
        ++counter.cnt;
        VirtualRegister newReg = new VirtualRegister(register.name + "." + counter.cnt, register.size);
        addedRegs.add(newReg);
        Pair<Register, Imm> addrPair = getRegAddress(stackRegister);
        rewrittenInstructions.add(
                new StoreInst(newReg, addrPair.getFirst(), addrPair.getSecond())
        );
        return newReg;
    }

    //改写asm函数
    void rewriteFunction() {
        //每个spillNode分配储存单元
        spillToStack = new HashMap<>();
        for (UncoloredNode spillNode : nodeSet.spilledNodes) {
            func.basicSpace += spillNode.register.size;
            StackRegister newReg = new StackRegister(func.basicSpace, spillNode.register.size);
            spillToStack.put(spillNode.register, new Pair<>(newReg, new Counter()));
        }
        //改写block
        //def和use创建新临时变量
        ArrayList<Register> use;
        Register def;
        Register rs;
        for (Block block : func.funcBlocks) {
            rewrittenInstructions = new LinkedList<>();
            for (ASMInstruction inst : block.instructions) {
                use = inst.getUse();
                def = inst.getDef();
                if (use != null) {
                    boolean flag = false;
                    for (int i = 0; i < use.size(); i++) {
                        rs = use.get(i);
                        if (spillToStack.containsKey(rs)) {
                            flag = true;
                            use.set(i, addLoad((VirtualRegister) rs));
                        }
                    }
                    if (flag) {
                        inst.setUse(use);
                    }
                }
                rewrittenInstructions.add(inst);
                if (def != null) {
                    if (spillToStack.containsKey(def)) {
                        def = addStore((VirtualRegister) def);
                        inst.setDef(def);
                    }
                }
            }
            block.instructions = rewrittenInstructions;
        }
    }

    /**
     * 将结点染色情况赋给reg
     */
    void assignColorToReg() {
        for (Map.Entry<Register, Node> entry : reg2node.entrySet()) {
            entry.getKey().color = entry.getValue().color;
        }
    }


}
