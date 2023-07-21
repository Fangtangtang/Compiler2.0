# Mx.g4
Mx语言的grammar，构建parse tree。
使用antlr生成Visitor接口，遍历parse tree，构建自定义的AST。
# AST
## ASTNode
AST上的所有结点，在结点内存该结点的子节点的信息（连接成树）、一些检查工作需要的成员和方法。

## ASTBuilder