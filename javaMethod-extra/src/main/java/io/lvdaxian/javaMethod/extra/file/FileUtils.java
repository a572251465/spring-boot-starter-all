package io.lvdaxian.javaMethod.extra.file;


import cn.hutool.core.util.StrUtil;

import java.io.File;

public class FileUtils {
  public static boolean ensureDir(String dirPath) {
    if (StrUtil.isEmpty(dirPath))
      return false;
    
    File file = new File(dirPath);
    // 文件存在
    if (file.exists()) {
      // 文件不是目录
      if (!file.isDirectory()) return false;
    } else
      // 能执行到这里，文件不存在
      file.mkdir();
    
    return true;
  }
}
