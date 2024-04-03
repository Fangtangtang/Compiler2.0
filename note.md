# opt-new
testcases/optim-new/adce-adv.mx
2000000
7211128




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

# ASM
```
clang-17 -S -emit-llvm --target=riscv32-unknown-elf  main.c -o main.ll            
```
```
 clang-17 -S main.ll --target=riscv32-unknown-elf -O0 -o main.s
```

## sample
### 函数基本结构
```
	.text
	.globl	func                        # -- Begin function func 说明函数全局可见
	.type	func,@function              # 说明func标签是个函数
func:                                   # 函数标签
# %bb.0:                        # 函数basicBlock标签
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	addi	a0, a0, 1
	sw	a0, -12(s0)
	lw	a0, -12(s0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end0:                    # 函数结束标签
	.size	func, .Lfunc_end0-func      #由offset计算出函数体所占字节数
                                        # -- End function
                                        
                                        
	.globl	main                            # -- Begin function main
	.type	main,@function
main:                                   # @main
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	addi	s0, sp, 16
	li	a0, 0
	sw	a0, -12(s0)
	li	a0, 3
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	call	func
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end1:
	.size	main, .Lfunc_end1-main
                                        # -- End function
	.ident	"Ubuntu clang version 17.0.0 (++20230814093516+04b49144ace0-1~exp1~20230814093619.21)"
	.section	".note.GNU-stack","",@progbits
	.addrsig
	.addrsig_sym func
```

### 入参
```
	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p1_m2p0_a2p1_c2p0"
	.file	"main.c"
	.globl	func                            # -- Begin function func
	.p2align	1
	.type	func,@function
func:                                   # @func
# %bb.0:
	addi	sp, sp, -48
	sw	ra, 44(sp)                      # 4-byte Folded Spill
	sw	s0, 40(sp)                      # 4-byte Folded Spill
	addi	s0, sp, 48
	lw	t0, 12(s0)                      # 本函数的起始前面部分为前一个函数的栈上入参，从栈顶（下）向栈底（上）分布
	lw	t0, 8(s0)
	lw	t0, 4(s0)
	lw	t0, 0(s0)                       # 最后一个入参
	sw	a0, -12(s0)
	sw	a1, -16(s0)
	sw	a2, -20(s0)
	sw	a3, -24(s0)
	sw	a4, -28(s0)
	sw	a5, -32(s0)
	sw	a6, -36(s0)
	sw	a7, -40(s0)
	lw	a0, 0(s0)
	lw	a1, 12(s0)
	add	a0, a0, a1
	lw	ra, 44(sp)                      # 4-byte Folded Reload
	lw	s0, 40(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 48
	ret
.Lfunc_end0:
	.size	func, .Lfunc_end0-func
                                        # -- End function
	.globl	main                            # -- Begin function main
	.p2align	1
	.type	main,@function
main:                                   # @main
# %bb.0:
	addi	sp, sp, -48
	sw	ra, 44(sp)                      # 4-byte Folded Spill
	sw	s0, 40(sp)                      # 4-byte Folded Spill
	addi	s0, sp, 48
	lw	a5, -12(s0)
	lw	a6, -16(s0)
	lw	a7, -20(s0)
	mv	a0, sp
	sw	a5, 12(a0)                      # 放在栈上的参数
	sw	a7, 8(a0)
	sw	a6, 4(a0)
	sw	a5, 0(a0)                       # 放不下的第一个入参
	mv	a0, a5                          # 可以放入寄存器的参数
	mv	a1, a5
	mv	a2, a6
	mv	a3, a7
	mv	a4, a5
	call	func
	sw	a0, -24(s0)
	lw	a0, -12(s0)
	addi	a0, a0, 1
	sw	a0, -28(s0)
	li	a0, 0
	lw	ra, 44(sp)                      # 4-byte Folded Reload
	lw	s0, 40(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 48
	ret
.Lfunc_end1:
	.size	main, .Lfunc_end1-main
                                        # -- End function
	.ident	"Ubuntu clang version 17.0.0 (++20230814093516+04b49144ace0-1~exp1~20230814093619.21)"
	.section	".note.GNU-stack","",@progbits
	.addrsig
	.addrsig_sym func

```
### 字符串常量
```
	.type	.L.str,@object                  # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1        # .rodata只读数据段
.L.str:
	.asciz	"123"
	.size	.L.str, 4

	.type	.L.str.1,@object                # @.str.1
.L.str.1:
	.asciz	"qwe"
	.size	.L.str.1, 4

```

```
	.type	.L.str,@object                  # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"123"
	.size	.L.str, 4

	.type	str1,@object                    # @str1
	.section	.sdata,"aw",@progbits                   # .sdata 全局变量 数据段
	.globl	str1
	.p2align	2
str1:
	.word	.L.str
	.size	str1, 4

	.type	.L.str.1,@object                # @.str.1
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str.1:
	.asciz	"qwe"
	.size	.L.str.1, 4

	.type	str2,@object                    # @str2
	.section	.sdata,"aw",@progbits
	.globl	str2
	.p2align	2
str2:
	.word	.L.str.1                        # 初始化值（被字符串常量初始化）
	.size	str2, 4

	.type	b,@object                       # @b
	.globl	b
	.p2align	2
b:
	.word	1                               # 0x1 初始化值
	.size	b, 4

	.type	a,@object                       # @a
	.section	.sbss,"aw",@nobits                      # .sbss被0初始化的全局变量
	.globl	a
	.p2align	2
a:
	.word	0                               # 0x0 初始化值
	.size	a, 4

```
### 全局变量
> .bss 子段中的数据在程序开始执行前被隐式地初始化为零，即设置为默认值
```
# bool
	.type	a,@object                       # @a
	.section	.sdata,"aw",@progbits
	.globl	a
a:
	.byte	1                               # 0x1
	.size	a, 1

	.type	b,@object                       # @b
	.section	.sbss,"aw",@nobits
	.globl	b
b:
	.byte	0                               # 0x0
	.size	b, 1

# int
	.type	c,@object                       # @c
	.globl	c
	.p2align	2
c:
	.word	0                               # 0x0 初值
	.size	c, 4                            # 占用字节数


```

```agsl

define dso_local noundef i32 @main() #0 {
  %1 = alloca ptr, align 4
  %2 = call noalias noundef nonnull ptr @_Znaj(i32 noundef 40) #2
  store ptr %2, ptr %1, align 4
  %3 = load ptr, ptr %1, align 4
  %4 = getelementptr inbounds i32, ptr %3, i32 1
  store i32 0, ptr %4, align 4
  ret i32 0
}

# .s
main:                                   # @main
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	li	a0, 40
	call	_Znaj                       # 调用函数
	sw	a0, -12(s0)                     # 存返回值 （数组首地址）store ptr %2, ptr %1
	lw	a1, -12(s0)                     # %3 = load ptr, ptr %1
	li	a0, 0
	sw	a0, 4(a1)                       # 计算出a[1]地址4(a1)，存值
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end0:
```