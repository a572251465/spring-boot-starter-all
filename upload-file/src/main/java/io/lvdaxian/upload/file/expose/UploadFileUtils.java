package io.lvdaxian.upload.file.expose;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public interface UploadFileUtils {
  MultipartFile convertFileToMultipartFile(File file, String filename);
  
  MultipartFile convertFileToMultipartFile(File file);
  
  MultipartFile getMultipartFileByName(String newFilename, String oldFilename);
  
  MultipartFile getMultipartFileByName(String newFilename);
  
  File getFileByName(String fileName);
  
  String getPathString(String fileName);
  
  Path getPath(String newFilename);
}
