package io.lvdaxian.middleware.whitelist.check.config;

import io.lvdaxian.middleware.whitelist.check.DoJoinPoint;
import io.lvdaxian.middleware.whitelist.check.context.WhitelistApplicationContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(WhiteListProperties.class)
@EnableConfigurationProperties(WhiteListProperties.class)
@Import({DoJoinPoint.class, WhitelistApplicationContext.class})
public class WhiteListAutoConfigure {
  
  @Bean("whiteListConfig")
  @ConditionalOnMissingBean
  public String whiteListConfig(WhiteListProperties properties) {
    return properties.getIntercept();
  }
}
