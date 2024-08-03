package io.lvdaxian.upload.file.functional;

@FunctionalInterface
public interface ConsumerP<T1, T2, T3> {
  void accept(T1 t1, T2 t2, T3 t3);
}
