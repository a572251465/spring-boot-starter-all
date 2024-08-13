package io.lvdaxian.javaMethod.extra.utils;


import java.io.File;

public class FileUtils {
  public static boolean ensureDir(String dirPath) {
    if (StringUtils.isEmpty(dirPath))
      return false;
    
    File file = new File(dirPath);
    if (file.exists())
      return file.isDirectory();
    else
      return file.mkdir();
  }
}
