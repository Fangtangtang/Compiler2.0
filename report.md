# 一个非常规compiler的踩雷报告
按着自己的理解和想法，在codegen阶段偏离了“正常”的路线，随后一发不可收拾。
在没有加任何优化的情况下，性能挺好，加了部分优化后，也有不错的表现，但最初并没有仔细考虑自己哪里写到不太一样，好在哪里，问题又在哪里。
但当想继续在llvm ir上加优化时，逐渐发现按照我的实现方式，llvm ir上加优化的效果非常差，因而反过头重新考虑我的设计实现问题……

借此机会先回顾整理一下这个compiler是如何成长的，又是如何“长歪”的。尝试探索一下它的非常规部分是否有其价值，主流的做法究竟好在哪里。
此外，对其“长歪”部分，有一些天马行空的新思路，也许会在整理报告期间考虑一下合理性或产生些想法，如果有可能，尝试实现并检测可行性。

## 成长历程
### Frontend
#### Build ParseTree
```java
        //char -> lexer
        MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new MxErrorListener());
        //lexer -> token
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //token -> parser
        MxParser parser = new MxLexer(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new MxErrorListener());
        //parse tree
        ParseTree parseTreeRoot = parser.program();
```
使用` Mx.g4` 文件定义语法规则的文件， 利用 `ANTLR` 能相应生成解析器、词法分析器、树遍历器等。

用 `ANTLR` 生成的 `MxLexer` 和 `MxParser` 来处理输入流（作为字符流），生成 `ParseTree` （program为源代码整体、解析的起始规则，对应树根）。
#### Build AST 
```java
        ASTBuilder astBuilder = new ASTBuilder();
        RootNode astRoot = (RootNode) astBuilder.visit(parseTreeRoot);
```

visitor mode，访问 `ParseTree` 结点（子树），子树accept，根据子树类型（和访问者类型）实现不同的访问逻辑。

ASTBuilder访问行为为根据 `ParseTree` 结构构建对应的 `AST` 。

##### AST Node
```java
abstract public class ASTNode {
    // 结点对应源代码中的位置
    public Position pos;

    //结点性质：直接所属的作用域
    public Scope scope = null;

    public ASTNode(Position pos) {
        this.pos = pos;
    }
    
    abstract public <T> T accept(ASTVisitor<? extends T> visitor);
}
```

#### Collect Symbol and Check Semantic
##### Collect Symbol 
将支持前向引用的`Symbol`收录进`SymbolTable`。

检查语法时，对支持前向引用的（mx中的`Class`和`Function`），需要先记录`SymbolName -> SymbolType`等，用于在语法检查到其定义之前检查其使用的合法性。

`SymbolCollector`访问`AST`上和前向引用部分对应的结点。
```java
        Scope.symbolTable = new SymbolTable();
        SymbolCollector symbolCollector = new SymbolCollector(Scope.symbolTable);
        symbolCollector.visit(astRoot);
```

##### Check Semantic
`SemanticChecker`访问`AST`，检查语法错误。

```java
        SemanticChecker semanticChecker = new SemanticChecker();
        semanticChecker.visit(astRoot);
```

非法Identifier等在parse阶段已被检出，该阶段主要检查以下几类。
- Multiple Definitions of(Class\Function\Variable)
- Undefined (Class\Function\Variable:include variable out of scope...)
- Type Mismatch(assign\binary expression\return type)
- Invaild Control Flow(invalid continue\break)
- Invalid Type(condtion isn't bool\index isn't int\++bool...)
- Missing Return Statement
- Array Dimension Out Of Bound(int[] a=new int[2]; a[1][1]=1;)

