package io.lvdaxian.middleware.whilelist.test.fallback;

import io.lvdaxian.middleware.whilelist.test.entity.UserInfo;

import java.util.function.Supplier;

public class WhitelistFallbackFactory implements Supplier<UserInfo> {
  @Override
  public UserInfo get() {
    return UserInfo.builder().id("999").name("降级用户").build();
  }
}
