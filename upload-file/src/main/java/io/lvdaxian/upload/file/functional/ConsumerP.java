package io.lvdaxian.upload.file.functional;

@FunctionalInterface
public interface ConsumerP<T1, T2, R> {
  R accept(T1 t1, T2 t2);
}
