package io.lvdaxian.upload.file.utils;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.file.Files.newInputStream;

public class FileUtils {
  /**
   * 创建目录事件
   *
   * @param dirPath 目录名称
   * @author lihh
   */
  public static void mkDir(String dirPath) {
    File dirPathFile = new File(dirPath);
    
    // 判断目录是否存在
    if (!(dirPathFile.exists() && dirPathFile.isDirectory()))
      dirPathFile.mkdir();
  }
  
  /**
   * 写文件的事件
   *
   * @param outputFilePath 输出的文件地址
   * @param inputStream    输入流
   * @author lihh
   */
  public static void writeFile(String outputFilePath, InputStream inputStream) {
    try {
      OutputStream outputStream = Files.newOutputStream(Paths.get(outputFilePath));
      
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, length);
      }
      
      outputStream.close();
      inputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * 合并多个文件
   *
   * @param files          文件数组
   * @param outputFilePath 合并文件路径
   * @author lihh
   */
  public static boolean mergeFile(File[] files, String outputFilePath) throws IOException {
    // 实例化 出力文件流
    // 使用APPEND选项打开或创建文件，并创建追加模式的输出流
    OutputStream outputStream = Files.newOutputStream(Paths.get(outputFilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    
    // 从这里 开始遍历输入文件
    for (File sourceFile : files) {
      // 判断文件是否存在
      if (!sourceFile.exists()) return false;
      
      // 这里是 输入流
      InputStream inputStream = newInputStream(sourceFile.toPath());
      
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, length);
      }
      outputStream.flush();
      inputStream.close();
    }
    
    if (outputStream != null) outputStream.close();
    
    return true;
  }
}
