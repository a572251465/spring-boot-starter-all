package io.lvdaxian.upload.file.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "io.lvdaxian.upload.file")
@Data
public class UploadFileProperties {
  private String enabledType;
  private String saveDir;
  private int httpInterceptorOrder;
  private String contextPrefix;
  private int delayConcurrencyMergeTime;
}
