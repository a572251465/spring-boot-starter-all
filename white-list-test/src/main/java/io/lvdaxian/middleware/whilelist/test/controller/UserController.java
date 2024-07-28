package io.lvdaxian.middleware.whilelist.test.controller;

import io.lvdaxian.middleware.whilelist.check.annotation.DoWhiteList;
import io.lvdaxian.middleware.whilelist.test.entity.UserInfo;
import io.lvdaxian.middleware.whilelist.test.fallback.WhitelistFallbackFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class UserController {
  
  private final AtomicInteger count = new AtomicInteger(0);
  
  @DoWhiteList(filterKey = "name", fallback = WhitelistFallbackFactory.class)
  @RequestMapping(path = "/api/queryUserInfo/{name}", method = RequestMethod.GET)
  public UserInfo queryUserInfo(@PathVariable("name") String name) {
    return UserInfo.builder().id(String.valueOf(count.decrementAndGet())).name(name).build();
  }
}
