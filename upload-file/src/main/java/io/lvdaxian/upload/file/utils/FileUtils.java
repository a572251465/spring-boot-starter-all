package io.lvdaxian.upload.file.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
   * 获取文件名称 不包括后缀
   *
   * @param filename 文件名称
   * @return 文件名称 不包括后缀
   * @author lihh
   */
  public static String getNameExcludeExt(String filename) {
    if (null == filename) return null;
    
    int idx = filename.length() - 1;
    for (; idx >= 0; idx -= 1) {
      char c = filename.charAt(idx);
      if ('.' == c)
        break;
    }
    
    return -1 == idx ? filename : filename.substring(0, idx);
  }
  
  /**
   * 删除存在的目录 以及文件
   *
   * @param fileOrDirPath 文件或是目的地址
   * @return boolean 是否删除成功
   * @author lihh
   */
  public static boolean deleteIfExists(String fileOrDirPath) {
    File file = new File(fileOrDirPath);
    
    if (!file.exists())
      return false;
    
    if (file.isFile())
      file.delete();
    else {
      File[] files = file.listFiles();
      
      if (files != null)
        Arrays.stream(files).forEach(File::delete);
      
      file.delete();
    }
    return true;
  }
  
  /**
   * 多个 path 拼接
   *
   * @param paths 多个目录
   * @return 返回拼接的 url
   * @author lihh
   */
  public static String joinPath(String... paths) {
    if (null == paths || 0 == paths.length) return "";
    
    List<String> pathList = Arrays.stream(paths).map(path -> {
      
      if (path.startsWith("/"))
        path = path.replaceAll("^/+", "");
      if (path.endsWith("/"))
        path = path.replaceAll("/+$", "");
      
      return path;
    }).collect(Collectors.toList());
    
    return String.join(File.separator, pathList);
  }
  
  /**
   * 读取文件列表
   *
   * @param dirPath 目录地址
   * @return 返回目录列表
   * @author lihh
   */
  public static File[] readDirectoryListing(String dirPath) {
    File file = new File(dirPath);
    
    if (!file.isDirectory()) return null;
    File[] files = file.listFiles();
    if (null == files || files.length == 0) return null;
    
    // 为了防止文件顺序乱了 这里进行强制排序
    Arrays.sort(files, (o1, o2) -> {
      String[] p1Arr = o1.getName().split("-"), p2Arr = o2.getName().split("-");
      int lastP1 = Integer.parseInt(p1Arr[1]), lastP2 = Integer.parseInt(p2Arr[1]);
      return lastP1 - lastP2;
    });
    
    return files;
  }
  
  /**
   * 判断文件是否存在
   *
   * @param filePath 文件路径
   * @return 返回 boolean 状态
   * @author lihh
   */
  public static boolean isFileExist(String filePath) {
    return new File(filePath).isFile();
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
