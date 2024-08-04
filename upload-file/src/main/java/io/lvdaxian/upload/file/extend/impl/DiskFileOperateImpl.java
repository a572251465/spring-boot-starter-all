package io.lvdaxian.upload.file.extend.impl;

import io.lvdaxian.upload.file.entity.UploadFileProperties;
import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.FileUtils;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "io.lvdaxian.upload.file.enabledType", havingValue = "disk")
public class DiskFileOperateImpl implements FileOperate {
  
  // 表示临时目录
  private static String tmpDir;
  private static String publicDir;
  
  @Resource
  private UploadFileProperties properties;
  
  @PostConstruct
  public void postHandler() {
    tmpDir = CommonUtils.getAbsolutePath(FileUtils.getFileRealPath(properties.getSaveDir())) + File.separator + "tmp";
    publicDir = CommonUtils.getAbsolutePath(FileUtils.getFileRealPath(properties.getSaveDir())) + File.separator + "public";
    // 基础目录
    String baseDir = CommonUtils.getAbsolutePath(FileUtils.getFileRealPath(properties.getSaveDir())) + "";
    
    FileUtils.mkDir(baseDir);
    FileUtils.mkDir(tmpDir);
    FileUtils.mkDir(publicDir);
  }
  
  /**
   * 文件 分片上传的逻辑
   *
   * @param file     上传的文件
   * @param baseDir  目录名称
   * @param filename 文件名称
   * @return 文件写入成功后 响应值
   * @author lihh
   */
  @Override
  public ResponseEntity upload(MultipartFile file, String baseDir, String filename) {
    baseDir = tmpDir + File.separator + baseDir;
    
    // 创建基础目录
    FileUtils.mkDir(baseDir);
    // 表示 文件地址
    String filePath = baseDir + File.separator + filename;
    
    // 将 文件 写入到磁盘中
    try {
      FileUtils.writeFile(filePath, file.getInputStream());
      return ResponseEntity.ok(true);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * 判断 文件是否存在
   *
   * @param filename 文件名称
   * @return 返回文件验证状态
   * @author lihh
   */
  @Override
  public ResponseEntity verify(String filename) {
    // 表示文件
    File file = new File(publicDir + File.separator + filename);
    return ResponseEntity.ok(file.isFile());
  }
  
  /**
   * 根据文件目录 拿到文件size 以及个数
   *
   * @param baseDir 文件目录
   * @return 返回文件size 以及临时文件个数
   * @author lihh
   */
  @Override
  public ResponseEntity list(String baseDir) {
    // 基础目录 file
    File basePathFile = new File(tmpDir + File.separator + baseDir);
    // 表示目录是否存在
    if (!basePathFile.isDirectory() || !basePathFile.exists())
      return ResponseEntity.ok(null);
    
    // 表示 fileSize
    long fileSize = 0;
    // 读取目录下的文件
    String[] list = Optional.ofNullable(basePathFile.list()).orElse(new String[]{});
    
    // 统计文件 字节大小
    for (String fileName : list)
      fileSize += new File(basePathFile + File.separator + fileName).length();
    return ResponseEntity.ok(Arrays.asList(list.length, fileSize));
  }
  
  /**
   * 根据 文件目录 以及文件名称 进行文件合并
   *
   * @param baseDir  文件基础目录
   * @param filename 文件名称
   * @return 文件合并结果
   * @author lihh
   */
  @Override
  public ResponseEntity merge(String baseDir, String filename) {
    baseDir = tmpDir + File.separator + baseDir;
    // 临时 基础目录
    File baseDirFile = new File(baseDir);
    
    // 读取目录
    File[] files = baseDirFile.listFiles();
    // 判断目录是否为空
    if (files == null || files.length == 0)
      return ResponseEntity.ok("");
    
    // 为了防止文件顺序乱了 这里进行强制排序
    Arrays.sort(files, new Comparator<File>() {
      @Override
      public int compare(File o1, File o2) {
        String[] p1Arr = o1.getName().split("-"), p2Arr = o2.getName().split("-");
        int lastP1 = Integer.parseInt(p1Arr[1]), lastP2 = Integer.parseInt(p2Arr[1]);
        return lastP1 - lastP2;
      }
    });
    
    // 表示合并后的目录
    String mergePublicDir = publicDir + File.separator + filename;
    // 判断是否合并成功
    boolean mergeFlag = false;
    try {
      mergeFlag = FileUtils.mergeFile(files, mergePublicDir);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    // 如果 merge成功后 删除临时文件
    if (mergeFlag) {
      // 先删除文件
      for (File file : files)
        if (file.isFile()) file.delete();
      
      // 删除目录本身
      try {
        Files.deleteIfExists(Paths.get(baseDir));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    
    return ResponseEntity.ok(mergeFlag);
  }
}
