package io.lvdaxian.middleware.whilelist.test.fallback;

import io.lvdaxian.middleware.whitelist.check.FallbackProcessor;
import io.lvdaxian.middleware.whilelist.test.entity.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class WhitelistFallbackFactory01 implements FallbackProcessor<UserInfo> {
  @Override
  public UserInfo handle() {
    return UserInfo.builder().id("999").name("降级用户").build();
  }
}
