# Compiler
## AST Build
### char -> parser
- **./src/parser**

利用antlr将程序字符按语法解析.

使用g4文件给出语法规则，利用antlr生成java类。
```text
//AST root
RootNode astRoot;

//char -> lexer
MxLexer lexer = new MxLexer(CharStreams.fromStream(inputStream)); 
lexer.removeErrorListeners(); 
lexer.addErrorListener(new MxErrorListener()); 
//lexer -> token
CommonTokenStream tokens = new CommonTokenStream(lexer);
//token -> parser
MxParser parser = new MxParser(tokens);
parser.removeErrorListeners();
parser.addErrorListener(new MxErrorListener());
//parse tree
ParseTree parseTreeRoot = parser.program();
```

### parser -> AST(Abstract Syntax Tree)
- **./src/ast**
  - ./type:AST上标示结点类型
  - ./expr:AST上表示expression
  - ./stmt:AST上表示statement
  - ./other:特殊
- **./src/frontend**：ASTBuilder

根据程序的语法树（遍历parse tree部分结点）构建AST。


```text
//AST Build
ASTBuilder astBuilder = new ASTBuilder();
astRoot = (RootNode) astBuilder.visit(parseTreeRoot);
```

## Semantic Check
- **./src/frontend**

### Symbol Collect
遍历AST结点，收集全局作用域内的类、函数。

扫两遍：
1. 收集所有的类名
2. 收集所有函数、类的成员，并检查函数的返回类型、参数类型

```text
Scope.symbolTable = new SymbolTable();
SymbolCollector symbolCollector = new SymbolCollector(Scope.symbolTable);
symbolCollector.visit(astRoot);
```
### Semantic Check
遍历所有的ASTNode，检查语义
```text
SemanticChecker semanticChecker = new SemanticChecker();
semanticChecker.visit(astRoot);
```
## IR Build
- **./src/ir**
- **./src/midend**
遍历AST构建LLVM IR。由alloca，为SSA形式，但每次使用局部变量需要load、store慢。
```text
IRBuilder irBuilder = new IRBuilder(Scope.symbolTable);
irBuilder.visit(astRoot);
```

## ASM Build

## Optimize
### IR
- inlining
- Mem2Reg
- DCE
- CCP

### ASM
- 图染色