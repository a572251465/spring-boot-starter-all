package io.lvdaxian.middleware.whilelist.check.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

// 通过 configuration-processor 来处理该注解
@ConfigurationProperties(prefix = "io.lvdaxian.whilelist")
@Getter
@Setter
public class WhiteListProperties {
  private String intercept;
}
