; ModuleID = 'builtin.c'
source_filename = "builtin.c"
target datalayout = "e-m:e-p:32:32-i64:64-n32-S128"
target triple = "riscv32-unknown-unknown-elf"

@.str = private unnamed_addr constant [3 x i8] c"%s\00", align 1
@.str.1 = private unnamed_addr constant [4 x i8] c"%s\0A\00", align 1
@.str.2 = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1

; Function Attrs: nounwind
define dso_local void @print(ptr noundef %0) local_unnamed_addr #0 {
  %2 = tail call i32 (ptr, ...) @printf(ptr noundef nonnull @.str, ptr noundef %0) #11
  ret void
}

declare dso_local i32 @printf(ptr noundef, ...) local_unnamed_addr #1

; Function Attrs: nounwind
define dso_local void @println(ptr noundef %0) local_unnamed_addr #0 {
  %2 = tail call i32 (ptr, ...) @printf(ptr noundef nonnull @.str.1, ptr noundef %0) #11
  ret void
}

; Function Attrs: nounwind
define dso_local void @printInt(i32 noundef %0) local_unnamed_addr #0 {
  %2 = tail call i32 (ptr, ...) @printf(ptr noundef nonnull @.str.2, i32 noundef %0) #11
  ret void
}

; Function Attrs: nounwind
define dso_local void @printlnInt(i32 noundef %0) local_unnamed_addr #0 {
  %2 = tail call i32 (ptr, ...) @printf(ptr noundef nonnull @.str.3, i32 noundef %0) #11
  ret void
}

; Function Attrs: nofree nounwind
define dso_local ptr @getString() local_unnamed_addr #2 {
  %1 = tail call dereferenceable_or_null(256) ptr @malloc(i32 noundef 256) #12
  %2 = tail call i32 (ptr, ...) @scanf(ptr noundef nonnull @.str, ptr noundef %1) #12
  ret ptr %1
}

; Function Attrs: mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite)
declare void @llvm.lifetime.start.p0(i64 immarg, ptr nocapture) #3

; Function Attrs: mustprogress nofree nounwind willreturn allockind("alloc,uninitialized") allocsize(0) memory(inaccessiblemem: readwrite)
declare dso_local noalias noundef ptr @malloc(i32 noundef) local_unnamed_addr #4

; Function Attrs: nofree nounwind
declare dso_local noundef i32 @scanf(ptr nocapture noundef readonly, ...) local_unnamed_addr #2

; Function Attrs: mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite)
declare void @llvm.lifetime.end.p0(i64 immarg, ptr nocapture) #3

; Function Attrs: nofree nounwind
define dso_local i32 @getInt() local_unnamed_addr #2 {
  %1 = alloca i32, align 4
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %1) #13
  %2 = call i32 (ptr, ...) @scanf(ptr noundef nonnull @.str.2, ptr noundef nonnull %1) #12
  %3 = load i32, ptr %1, align 4, !tbaa !4
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %1) #13
  ret i32 %3
}

; Function Attrs: nofree nounwind
define dso_local noalias ptr @toString(i32 noundef %0) local_unnamed_addr #2 {
  %2 = tail call dereferenceable_or_null(256) ptr @malloc(i32 noundef 256) #12
  %3 = tail call i32 (ptr, ptr, ...) @sprintf(ptr noundef nonnull dereferenceable(1) %2, ptr noundef nonnull dereferenceable(1) @.str.2, i32 noundef %0) #12
  ret ptr %2
}

; Function Attrs: nofree nounwind
declare dso_local noundef i32 @sprintf(ptr noalias nocapture noundef writeonly, ptr nocapture noundef readonly, ...) local_unnamed_addr #2

; Function Attrs: mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: read)
define dso_local i32 @_array.size(ptr nocapture noundef readonly %0) local_unnamed_addr #5 {
  %2 = getelementptr inbounds i32, ptr %0, i32 -1
  %3 = load i32, ptr %2, align 4, !tbaa !4
  ret i32 %3
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
define dso_local i32 @_string.length(ptr nocapture noundef readonly %0) local_unnamed_addr #6 {
  %2 = tail call i32 @strlen(ptr noundef nonnull dereferenceable(1) %0) #12
  ret i32 %2
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
declare dso_local i32 @strlen(ptr nocapture noundef) local_unnamed_addr #6

; Function Attrs: nounwind
define dso_local ptr @_string.substring(ptr noundef %0, i32 noundef %1, i32 noundef %2) local_unnamed_addr #0 {
  %4 = sub nsw i32 %2, %1
  %5 = add nsw i32 %4, 1
  %6 = tail call ptr @malloc(i32 noundef %5) #12
  %7 = getelementptr inbounds i8, ptr %0, i32 %1
  %8 = tail call ptr @memcpy(ptr noundef %6, ptr noundef %7, i32 noundef %5) #11
  %9 = getelementptr inbounds i8, ptr %6, i32 %4
  store i8 0, ptr %9, align 1, !tbaa !8
  ret ptr %6
}

declare dso_local ptr @memcpy(ptr noundef, ptr noundef, i32 noundef) local_unnamed_addr #1

; Function Attrs: nofree nounwind
define dso_local i32 @_string.parseInt(ptr nocapture noundef readonly %0) local_unnamed_addr #2 {
  %2 = alloca i32, align 4
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %2) #13
  %3 = call i32 (ptr, ptr, ...) @sscanf(ptr noundef %0, ptr noundef nonnull @.str.2, ptr noundef nonnull %2) #12
  %4 = load i32, ptr %2, align 4, !tbaa !4
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %2) #13
  ret i32 %4
}

; Function Attrs: nofree nounwind
declare dso_local noundef i32 @sscanf(ptr nocapture noundef readonly, ptr nocapture noundef readonly, ...) local_unnamed_addr #2

; Function Attrs: mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: read)
define dso_local i32 @_string.ord(ptr nocapture noundef readonly %0, i32 noundef %1) local_unnamed_addr #5 {
  %3 = getelementptr inbounds i8, ptr %0, i32 %1
  %4 = load i8, ptr %3, align 1, !tbaa !8
  %5 = zext i8 %4 to i32
  ret i32 %5
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
define dso_local zeroext i1 @_string.equal(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #6 {
  %3 = tail call i32 @strcmp(ptr noundef nonnull dereferenceable(1) %0, ptr noundef nonnull dereferenceable(1) %1) #12
  %4 = icmp eq i32 %3, 0
  ret i1 %4
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
declare dso_local i32 @strcmp(ptr nocapture noundef, ptr nocapture noundef) local_unnamed_addr #6

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
define dso_local zeroext i1 @_string.notEqual(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #6 {
  %3 = tail call i32 @strcmp(ptr noundef nonnull dereferenceable(1) %0, ptr noundef nonnull dereferenceable(1) %1) #12
  %4 = icmp ne i32 %3, 0
  ret i1 %4
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
define dso_local zeroext i1 @_string.less(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #6 {
  %3 = tail call i32 @strcmp(ptr noundef nonnull dereferenceable(1) %0, ptr noundef nonnull dereferenceable(1) %1) #12
  %4 = icmp slt i32 %3, 0
  ret i1 %4
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
define dso_local zeroext i1 @_string.lessOrEqual(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #6 {
  %3 = tail call i32 @strcmp(ptr noundef nonnull dereferenceable(1) %0, ptr noundef nonnull dereferenceable(1) %1) #12
  %4 = icmp slt i32 %3, 1
  ret i1 %4
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
define dso_local zeroext i1 @_string.greater(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #6 {
  %3 = tail call i32 @strcmp(ptr noundef nonnull dereferenceable(1) %0, ptr noundef nonnull dereferenceable(1) %1) #12
  %4 = icmp sgt i32 %3, 0
  ret i1 %4
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: read)
define dso_local zeroext i1 @_string.greaterOrEqual(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #6 {
  %3 = tail call i32 @strcmp(ptr noundef nonnull dereferenceable(1) %0, ptr noundef nonnull dereferenceable(1) %1) #12
  %4 = icmp sgt i32 %3, -1
  ret i1 %4
}

; Function Attrs: mustprogress nofree nounwind willreturn
define dso_local ptr @_string.add(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #7 {
  %3 = tail call i32 @strlen(ptr noundef nonnull dereferenceable(1) %0) #12
  %4 = tail call i32 @strlen(ptr noundef nonnull dereferenceable(1) %1) #12
  %5 = add i32 %3, 1
  %6 = add i32 %5, %4
  %7 = tail call ptr @malloc(i32 noundef %6) #12
  %8 = tail call ptr @strcpy(ptr noundef nonnull dereferenceable(1) %7, ptr noundef nonnull dereferenceable(1) %0) #12
  %9 = tail call ptr @strcat(ptr noundef nonnull dereferenceable(1) %7, ptr noundef nonnull dereferenceable(1) %1) #12
  ret ptr %7
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: readwrite)
declare dso_local ptr @strcpy(ptr noalias noundef returned writeonly, ptr noalias nocapture noundef readonly) local_unnamed_addr #8

; Function Attrs: mustprogress nofree nounwind willreturn memory(argmem: readwrite)
declare dso_local ptr @strcat(ptr noalias noundef returned, ptr noalias nocapture noundef readonly) local_unnamed_addr #8

; Function Attrs: mustprogress nofree nounwind willreturn memory(inaccessiblemem: readwrite)
define dso_local noalias ptr @_malloc(i32 noundef %0) local_unnamed_addr #9 {
  %2 = tail call ptr @malloc(i32 noundef %0) #12
  ret ptr %2
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(write, argmem: none, inaccessiblemem: readwrite)
define dso_local noalias nonnull ptr @_malloc_array(i32 noundef %0, i32 noundef %1) local_unnamed_addr #10 {
  %3 = mul nsw i32 %1, %0
  %4 = add nsw i32 %3, 4
  %5 = tail call ptr @malloc(i32 noundef %4) #12
  store i32 %1, ptr %5, align 4, !tbaa !4
  %6 = getelementptr inbounds i32, ptr %5, i32 1
  ret ptr %6
}

attributes #0 = { nounwind "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #1 = { "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #2 = { nofree nounwind "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #3 = { mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite) }
attributes #4 = { mustprogress nofree nounwind willreturn allockind("alloc,uninitialized") allocsize(0) memory(inaccessiblemem: readwrite) "alloc-family"="malloc" "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #5 = { mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: read) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #6 = { mustprogress nofree nounwind willreturn memory(argmem: read) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #7 = { mustprogress nofree nounwind willreturn "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #8 = { mustprogress nofree nounwind willreturn memory(argmem: readwrite) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #9 = { mustprogress nofree nounwind willreturn memory(inaccessiblemem: readwrite) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #10 = { mustprogress nofree nounwind willreturn memory(write, argmem: none, inaccessiblemem: readwrite) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-smaia,-experimental-ssaia,-experimental-zacas,-experimental-zfa,-experimental-zfbfmin,-experimental-zicond,-experimental-zihintntl,-experimental-ztso,-experimental-zvbb,-experimental-zvbc,-experimental-zvfbfmin,-experimental-zvfbfwma,-experimental-zvkg,-experimental-zvkn,-experimental-zvknc,-experimental-zvkned,-experimental-zvkng,-experimental-zvknha,-experimental-zvknhb,-experimental-zvks,-experimental-zvksc,-experimental-zvksed,-experimental-zvksg,-experimental-zvksh,-experimental-zvkt,-f,-h,-save-restore,-svinval,-svnapot,-svpbmt,-v,-xcvbitmanip,-xcvmac,-xsfcie,-xsfvcp,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zicbom,-zicbop,-zicboz,-zicntr,-zicsr,-zifencei,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #11 = { nobuiltin nounwind "no-builtin-memcpy" "no-builtin-printf" }
attributes #12 = { "no-builtin-memcpy" "no-builtin-printf" }
attributes #13 = { nounwind }

!llvm.module.flags = !{!0, !1, !2}
!llvm.ident = !{!3}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{i32 1, !"target-abi", !"ilp32"}
!2 = !{i32 8, !"SmallDataLimit", i32 8}
!3 = !{!"Ubuntu clang version 17.0.0 (++20230814093516+04b49144ace0-1~exp1~20230814093619.21)"}
!4 = !{!5, !5, i64 0}
!5 = !{!"int", !6, i64 0}
!6 = !{!"omnipotent char", !7, i64 0}
!7 = !{!"Simple C/C++ TBAA"}
!8 = !{!6, !6, i64 0}
