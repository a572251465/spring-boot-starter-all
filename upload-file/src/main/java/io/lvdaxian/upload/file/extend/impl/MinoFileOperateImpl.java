package io.lvdaxian.upload.file.extend.impl;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

@ConditionalOnProperty(
    prefix = "io.lvdaxian.upload.file",
    name = "enabledType",
    havingValue = "Mino"
)
@ConditionalOnClass({MinioClient.class})
@Configuration
public class MinoFileOperateImpl implements FileOperate {
  
  @Override
  public ResponseEntity upload(MultipartFile file, String baseDir, String filename) {
    return null;
  }
  
  @Override
  public ResponseEntity verify(String filename) {
    return null;
  }
  
  @Override
  public ResponseEntity list(String baseDir) {
    return null;
  }
  
  @Override
  public ResponseEntity merge(String baseDir, String filename) {
    return null;
  }
}
