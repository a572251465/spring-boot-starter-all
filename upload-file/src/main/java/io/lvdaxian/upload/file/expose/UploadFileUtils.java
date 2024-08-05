package io.lvdaxian.upload.file.expose;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface UploadFileUtils {
  MultipartFile convertFileToMultipartFile(File file, String newName);
  
  MultipartFile convertFileToMultipartFile(File file);
  
  MultipartFile getMultipartFileByName(String fileName);
  File getFileByName(String fileName);
}
