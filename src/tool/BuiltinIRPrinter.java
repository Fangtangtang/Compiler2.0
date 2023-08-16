package tool;

import java.io.IOException;
import java.io.PrintStream;

/**
 * @author F
 * 负责将内置函数的.ll文件拼接
 */
public class BuiltinIRPrinter {
    String builtin =
            """
                            ; ModuleID = 'builtin.c'
                            source_filename = "builtin.c"
                            target datalayout = "e-m:e-p:32:32-i64:64-n32-S128"
                            target triple = "riscv32-unknown-unknown-elf"
                                        
                            @.str = private unnamed_addr constant [3 x i8] c"%s\\00", align 1
                            @.str.1 = private unnamed_addr constant [4 x i8] c"%s\\0A\\00", align 1
                            @.str.2 = private unnamed_addr constant [3 x i8] c"%d\\00", align 1
                            @.str.3 = private unnamed_addr constant [4 x i8] c"%d\\0A\\00", align 1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local void @print(ptr noundef %0) #0 {
                              %2 = alloca ptr, align 4
                              store ptr %0, ptr %2, align 4
                              %3 = load ptr, ptr %2, align 4
                              %4 = call i32 (ptr, ...) @printf(ptr noundef @.str, ptr noundef %3) #2
                              ret void
                            }
                                        
                            declare dso_local i32 @printf(ptr noundef, ...) #1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local void @println(ptr noundef %0) #0 {
                              %2 = alloca ptr, align 4
                              store ptr %0, ptr %2, align 4
                              %3 = load ptr, ptr %2, align 4
                              %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.1, ptr noundef %3) #2
                              ret void
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local void @printInt(i32 noundef %0) #0 {
                              %2 = alloca i32, align 4
                              store i32 %0, ptr %2, align 4
                              %3 = load i32, ptr %2, align 4
                              %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.2, i32 noundef %3) #2
                              ret void
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local void @printlnInt(i32 noundef %0) #0 {
                              %2 = alloca i32, align 4
                              store i32 %0, ptr %2, align 4
                              %3 = load i32, ptr %2, align 4
                              %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.3, i32 noundef %3) #2
                              ret void
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local ptr @getString() #0 {
                              %1 = alloca ptr, align 4
                              %2 = call ptr @malloc(i32 noundef 256) #3
                              store ptr %2, ptr %1, align 4
                              %3 = load ptr, ptr %1, align 4
                              %4 = call i32 (ptr, ...) @scanf(ptr noundef @.str, ptr noundef %3) #3
                              %5 = load ptr, ptr %1, align 4
                              ret ptr %5
                            }
                                        
                            declare dso_local ptr @malloc(i32 noundef) #1
                                        
                            declare dso_local i32 @scanf(ptr noundef, ...) #1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local i32 @getInt() #0 {
                              %1 = alloca i32, align 4
                              %2 = call i32 (ptr, ...) @scanf(ptr noundef @.str.2, ptr noundef %1) #3
                              %3 = load i32, ptr %1, align 4
                              ret i32 %3
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local ptr @toString(i32 noundef %0) #0 {
                              %2 = alloca i32, align 4
                              %3 = alloca ptr, align 4
                              store i32 %0, ptr %2, align 4
                              %4 = call ptr @malloc(i32 noundef 256) #3
                              store ptr %4, ptr %3, align 4
                              %5 = load ptr, ptr %3, align 4
                              %6 = load i32, ptr %2, align 4
                              %7 = call i32 (ptr, ptr, ...) @sprintf(ptr noundef %5, ptr noundef @.str.2, i32 noundef %6) #3
                              %8 = load ptr, ptr %3, align 4
                              ret ptr %8
                            }
                                        
                            declare dso_local i32 @sprintf(ptr noundef, ptr noundef, ...) #1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local i32 @_string.length(ptr noundef %0) #0 {
                              %2 = alloca ptr, align 4
                              store ptr %0, ptr %2, align 4
                              %3 = load ptr, ptr %2, align 4
                              %4 = call i32 @strlen(ptr noundef %3) #3
                              ret i32 %4
                            }
                                        
                            declare dso_local i32 @strlen(ptr noundef) #1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local ptr @_string.substring(ptr noundef %0, i32 noundef %1, i32 noundef %2) #0 {
                              %4 = alloca ptr, align 4
                              %5 = alloca i32, align 4
                              %6 = alloca i32, align 4
                              %7 = alloca i32, align 4
                              %8 = alloca ptr, align 4
                              store ptr %0, ptr %4, align 4
                              store i32 %1, ptr %5, align 4
                              store i32 %2, ptr %6, align 4
                              %9 = load i32, ptr %6, align 4
                              %10 = load i32, ptr %5, align 4
                              %11 = sub nsw i32 %9, %10
                              %12 = add nsw i32 %11, 1
                              store i32 %12, ptr %7, align 4
                              %13 = load i32, ptr %7, align 4
                              %14 = mul i32 1, %13
                              %15 = call ptr @malloc(i32 noundef %14) #3
                              store ptr %15, ptr %8, align 4
                              %16 = load ptr, ptr %8, align 4
                              %17 = load ptr, ptr %4, align 4
                              %18 = load i32, ptr %5, align 4
                              %19 = getelementptr inbounds i8, ptr %17, i32 %18
                              %20 = load i32, ptr %7, align 4
                              %21 = call ptr @memcpy(ptr noundef %16, ptr noundef %19, i32 noundef %20) #2
                              %22 = load ptr, ptr %8, align 4
                              %23 = load i32, ptr %7, align 4
                              %24 = sub nsw i32 %23, 1
                              %25 = getelementptr inbounds i8, ptr %22, i32 %24
                              store i8 0, ptr %25, align 1
                              %26 = load ptr, ptr %8, align 4
                              ret ptr %26
                            }
                                        
                            declare dso_local ptr @memcpy(ptr noundef, ptr noundef, i32 noundef) #1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local i32 @_string.parseInt(ptr noundef %0) #0 {
                              %2 = alloca ptr, align 4
                              %3 = alloca i32, align 4
                              store ptr %0, ptr %2, align 4
                              %4 = load ptr, ptr %2, align 4
                              %5 = call i32 (ptr, ptr, ...) @sscanf(ptr noundef %4, ptr noundef @.str.2, ptr noundef %3) #3
                              %6 = load i32, ptr %3, align 4
                              ret i32 %6
                            }
                                        
                            declare dso_local i32 @sscanf(ptr noundef, ptr noundef, ...) #1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local i32 @_string.ord(ptr noundef %0, i32 noundef %1) #0 {
                              %3 = alloca ptr, align 4
                              %4 = alloca i32, align 4
                              store ptr %0, ptr %3, align 4
                              store i32 %1, ptr %4, align 4
                              %5 = load ptr, ptr %3, align 4
                              %6 = load i32, ptr %4, align 4
                              %7 = getelementptr inbounds i8, ptr %5, i32 %6
                              %8 = load i8, ptr %7, align 1
                              %9 = zext i8 %8 to i32
                              ret i32 %9
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local zeroext i1 @_string.equal(ptr noundef %0, ptr noundef %1) #0 {
                              %3 = alloca ptr, align 4
                              %4 = alloca ptr, align 4
                              store ptr %0, ptr %3, align 4
                              store ptr %1, ptr %4, align 4
                              %5 = load ptr, ptr %3, align 4
                              %6 = load ptr, ptr %4, align 4
                              %7 = call i32 @strcmp(ptr noundef %5, ptr noundef %6) #3
                              %8 = icmp eq i32 %7, 0
                              ret i1 %8
                            }
                                        
                            declare dso_local i32 @strcmp(ptr noundef, ptr noundef) #1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local zeroext i1 @_string.notEqual(ptr noundef %0, ptr noundef %1) #0 {
                              %3 = alloca ptr, align 4
                              %4 = alloca ptr, align 4
                              store ptr %0, ptr %3, align 4
                              store ptr %1, ptr %4, align 4
                              %5 = load ptr, ptr %3, align 4
                              %6 = load ptr, ptr %4, align 4
                              %7 = call i32 @strcmp(ptr noundef %5, ptr noundef %6) #3
                              %8 = icmp ne i32 %7, 0
                              ret i1 %8
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local zeroext i1 @_string.less(ptr noundef %0, ptr noundef %1) #0 {
                              %3 = alloca ptr, align 4
                              %4 = alloca ptr, align 4
                              store ptr %0, ptr %3, align 4
                              store ptr %1, ptr %4, align 4
                              %5 = load ptr, ptr %3, align 4
                              %6 = load ptr, ptr %4, align 4
                              %7 = call i32 @strcmp(ptr noundef %5, ptr noundef %6) #3
                              %8 = icmp slt i32 %7, 0
                              ret i1 %8
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local zeroext i1 @_string.lessOrEqual(ptr noundef %0, ptr noundef %1) #0 {
                              %3 = alloca ptr, align 4
                              %4 = alloca ptr, align 4
                              store ptr %0, ptr %3, align 4
                              store ptr %1, ptr %4, align 4
                              %5 = load ptr, ptr %3, align 4
                              %6 = load ptr, ptr %4, align 4
                              %7 = call i32 @strcmp(ptr noundef %5, ptr noundef %6) #3
                              %8 = icmp sle i32 %7, 0
                              ret i1 %8
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local zeroext i1 @_string.greater(ptr noundef %0, ptr noundef %1) #0 {
                              %3 = alloca ptr, align 4
                              %4 = alloca ptr, align 4
                              store ptr %0, ptr %3, align 4
                              store ptr %1, ptr %4, align 4
                              %5 = load ptr, ptr %3, align 4
                              %6 = load ptr, ptr %4, align 4
                              %7 = call i32 @strcmp(ptr noundef %5, ptr noundef %6) #3
                              %8 = icmp sgt i32 %7, 0
                              ret i1 %8
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local zeroext i1 @_string.greaterOrEqual(ptr noundef %0, ptr noundef %1) #0 {
                              %3 = alloca ptr, align 4
                              %4 = alloca ptr, align 4
                              store ptr %0, ptr %3, align 4
                              store ptr %1, ptr %4, align 4
                              %5 = load ptr, ptr %3, align 4
                              %6 = load ptr, ptr %4, align 4
                              %7 = call i32 @strcmp(ptr noundef %5, ptr noundef %6) #3
                              %8 = icmp sge i32 %7, 0
                              ret i1 %8
                            }
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local ptr @_string.add(ptr noundef %0, ptr noundef %1) #0 {
                              %3 = alloca ptr, align 4
                              %4 = alloca ptr, align 4
                              %5 = alloca ptr, align 4
                              store ptr %0, ptr %3, align 4
                              store ptr %1, ptr %4, align 4
                              %6 = load ptr, ptr %3, align 4
                              %7 = call i32 @strlen(ptr noundef %6) #3
                              %8 = load ptr, ptr %4, align 4
                              %9 = call i32 @strlen(ptr noundef %8) #3
                              %10 = add nsw i32 %7, %9
                              %11 = add nsw i32 %10, 1
                              %12 = mul i32 1, %11
                              %13 = call ptr @malloc(i32 noundef %12) #3
                              store ptr %13, ptr %5, align 4
                              %14 = load ptr, ptr %5, align 4
                              %15 = load ptr, ptr %3, align 4
                              %16 = call ptr @strcpy(ptr noundef %14, ptr noundef %15) #3
                              %17 = load ptr, ptr %5, align 4
                              %18 = load ptr, ptr %4, align 4
                              %19 = call ptr @strcat(ptr noundef %17, ptr noundef %18) #3
                              %20 = load ptr, ptr %5, align 4
                              ret ptr %20
                            }
                                        
                            declare dso_local ptr @strcpy(ptr noundef, ptr noundef) #1
                                        
                            declare dso_local ptr @strcat(ptr noundef, ptr noundef) #1
                                        
                            ; Function Attrs: noinline nounwind optnone
                            define dso_local ptr @_malloc(i32 noundef %0) #0 {
                              %2 = alloca i32, align 4
                              store i32 %0, ptr %2, align 4
                              %3 = load i32, ptr %2, align 4
                              %4 = call ptr @malloc(i32 noundef %3) #3
                              ret ptr %4
                            }
                                        
                            attributes #0 = { noinline nounwind optnone "frame-pointer"="all" "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
                            attributes #1 = { "frame-pointer"="all" "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
                            attributes #2 = { nobuiltin "no-builtin-memcpy" "no-builtin-printf" }
                            attributes #3 = { "no-builtin-memcpy" "no-builtin-printf" }
                                        
                            !llvm.module.flags = !{!0, !1, !2, !3}
                            !llvm.ident = !{!4}
                                        
                            !0 = !{i32 1, !"wchar_size", i32 4}
                            !1 = !{i32 1, !"target-abi", !"ilp32"}
                            !2 = !{i32 7, !"frame-pointer", i32 2}
                            !3 = !{i32 8, !"SmallDataLimit", i32 8}
                            !4 = !{!"Ubuntu clang version 17.0.0 (++20230814093516+04b49144ace0-1~exp1~20230814093619.21)"}
                                        
                    """;

    public BuiltinIRPrinter(PrintStream out) throws IOException {
        out.write(builtin.getBytes());
    }
}
