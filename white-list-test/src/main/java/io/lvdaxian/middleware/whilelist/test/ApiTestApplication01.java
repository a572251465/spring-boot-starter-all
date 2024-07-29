package io.lvdaxian.middleware.whilelist.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"io.lvdaxian.middleware.whitelist.check"})
public class ApiTestApplication01 {
  public static void main(String[] args) {
    SpringApplication.run(ApiTestApplication01.class, args);
  }
}
