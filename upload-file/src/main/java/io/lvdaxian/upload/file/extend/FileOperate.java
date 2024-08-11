package io.lvdaxian.upload.file.extend;

import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileOperate {
  ResponseEntity upload(MultipartFile file, String baseDir, String filename);
  
  ResponseEntity verify(String filename);
  
  ResponseEntity list(String baseDir);
  
  ResponseEntity merge(String baseDir, String filename);
  int partMerge(int readIdx, String filename);
}
