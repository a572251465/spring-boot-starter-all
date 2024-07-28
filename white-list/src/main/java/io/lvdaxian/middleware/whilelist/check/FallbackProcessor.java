package io.lvdaxian.middleware.whilelist.check;

public interface FallbackProcessor<T> {
  T handle();
}
