package io.lvdaxian.upload.file.extend.impl;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@ConditionalOnProperty(
    name = "io.lvdaxian.upload.file.enabledType",
    havingValue = "disk"
)
public class DiskFileOperateImpl implements FileOperate {
  
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
