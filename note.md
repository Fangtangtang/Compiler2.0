# Mx.g4
Mx语言的grammar，构建parse tree。
使用antlr生成Visitor接口，遍历parse tree，构建自定义的AST。
# AST
## ASTNode
AST上的所有结点，在结点内存该结点的子节点的信息（连接成树）、一些检查工作需要的成员和方法。

## ASTBuilder

# IR
```
clang-17 -emit-llvm --target=riscv32-unknown-elf -S -O2 -fno-builtin-printf -fno-builtin-memcpy \builtin.c -o builtin_intermediate.ll
sed 's/string_/string./g;s/array_/array./g' builtin_intermediate.ll > builtin.ll
rm builtin_intermediate.ll

```
```
clang-17 -emit-llvm --target=riscv32-unknown-elf -S -O2 -fno-builtin-printf -fno-builtin-memcpy \builtin.c -o builtin.ll

```
```
testcases/codegen/scripts/test_llvm_ir.bash 'bin/mxc -emit-llvm' testcases/codegen/t1.mx bin/builtin.ll 
```
```
 clang-17 ./bin/builtin.ll /tmp/mxc.naTyTyxf1f/output.ll -o code 
 ./code
```