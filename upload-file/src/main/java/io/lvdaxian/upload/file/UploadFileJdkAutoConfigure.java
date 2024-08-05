package io.lvdaxian.upload.file;

import io.lvdaxian.upload.file.entity.UploadFileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(UploadFileProperties.class)
@EnableConfigurationProperties(UploadFileProperties.class)
@Configuration
@ComponentScan(basePackages = "io.lvdaxian.upload.file")
public class UploadFileJdkAutoConfigure {
}
