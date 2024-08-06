package io.lvdaxian.upload.file;

import io.lvdaxian.upload.file.entity.UploadFileFullProperties;
import io.lvdaxian.upload.file.entity.UploadFileProperties;
import io.lvdaxian.upload.file.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(UploadFileProperties.class)
@ConditionalOnMissingBean({UploadFileFullProperties.class, UploadFileProperties.class})
@ConditionalOnProperty(
    prefix = "io.lvdaxian.upload.file",
    name = "enabled-type",
    havingValue = "disk"
)
@Configuration
@ComponentScan(basePackages = "io.lvdaxian.upload.file")
public class UploadFileJdkAutoConfigure {
  
  private static final Logger log = LoggerFactory.getLogger(UploadFileJdkAutoConfigure.class);
  
  @Bean
  public UploadFileFullProperties uploadFileFullProperties(UploadFileProperties properties) {
    log.info(CommonUtils.getCommonPrefixAndSuffix("init start"));
    return new UploadFileFullProperties(properties);
  }
}
