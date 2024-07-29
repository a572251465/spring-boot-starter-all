package io.lvdaxian.middleware.whitelist.check;

public interface FallbackProcessor<T> {
  T handle();
}
