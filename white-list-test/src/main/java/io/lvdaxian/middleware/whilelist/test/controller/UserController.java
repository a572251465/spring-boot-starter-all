package io.lvdaxian.middleware.whilelist.test.controller;

import io.lvdaxian.middleware.whitelist.check.annotation.DoWhiteList;
import io.lvdaxian.middleware.whilelist.test.entity.UserInfo;
import io.lvdaxian.middleware.whilelist.test.fallback.WhitelistFallbackFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class UserController {
  
  private final AtomicInteger count = new AtomicInteger(0);
  
  @DoWhiteList(filterKey = "name", fallback = WhitelistFallbackFactory.class)
  @RequestMapping(path = "/api/queryUserInfo/{name}", method = RequestMethod.GET)
  public UserInfo queryUserInfo(@PathVariable("name") String name) {
    return UserInfo.builder().id(String.valueOf(count.decrementAndGet())).name(name).build();
  }
  
  @PostMapping("/api/queryUserInfo01")
  @DoWhiteList(filterKey = "name", fallback = WhitelistFallbackFactory.class)
  public UserInfo queryUserInfo01(@RequestBody UserInfo info) {
    return info;
  }
  
  @PostMapping("/api/queryUserInfo02")
  @DoWhiteList(filterKey = "name", fallback = WhitelistFallbackFactory.class)
  public UserInfo queryUserInfo02(@RequestBody Map<String, String> map) {
    return UserInfo.builder().id(map.get("id")).name(map.get("name")).build();
  }
  
}
