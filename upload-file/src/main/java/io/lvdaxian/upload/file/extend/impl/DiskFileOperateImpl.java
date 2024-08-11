package io.lvdaxian.upload.file.extend.impl;

import cn.hutool.core.io.FileUtil;
import io.lvdaxian.upload.file.entity.UploadFileFullProperties;
import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.notify.NotifyCenter;
import io.lvdaxian.upload.file.notify.entity.Event;
import io.lvdaxian.upload.file.notify.enumeration.PublisherTypeEnum;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.Constants;
import io.lvdaxian.upload.file.utils.FileUtils;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "io.lvdaxian.upload.file.enabled-type", havingValue = "disk")
public class DiskFileOperateImpl implements FileOperate {
  
  @Resource
  private UploadFileFullProperties fullProperties;
  
  @PostConstruct
  public void postHandler() {
    // 基础目录
    String baseDir = fullProperties.setBaseDir(CommonUtils.getAbsolutePath(fullProperties.getInnerProperties().getSaveDir()).toString());
    String tmpDir = fullProperties.setTmpDir(baseDir + File.separator + Constants.CONST_TMP_NAME);
    String publicDir = fullProperties.setPublicDir(baseDir + File.separator + Constants.CONST_PUBLIC_NAME);
    String convertDir = fullProperties.setConvertDir(baseDir + File.separator + Constants.CONST_CONVERT_NAME);
    
    FileUtils.mkDir(baseDir);
    FileUtils.mkDir(tmpDir);
    FileUtils.mkDir(publicDir);
    FileUtils.mkDir(convertDir);
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
    baseDir = fullProperties.getTmpDir() + File.separator + baseDir;
    
    // 创建基础目录
    FileUtils.mkDir(baseDir);
    // 表示 文件地址
    String filePath = baseDir + File.separator + filename;
    
    // 将 文件 写入到磁盘中
    try {
      FileUtils.writeFile(filePath, file.getInputStream());
      
      if (!filename.startsWith("not_del_file")) {
        String newFilename = filename.split("-")[0];
        // 触发订阅
        NotifyCenter.publishEvent(PublisherTypeEnum.firingTask, Event.builder().id(newFilename).build());
      }
      
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
    return ResponseEntity.ok(FileUtils.isFileExist(fullProperties.getPublicDir() + File.separator + filename));
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
    File basePathFile = new File(fullProperties.getTmpDir() + File.separator + baseDir);
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
    baseDir = fullProperties.getTmpDir() + File.separator + baseDir;
    
    // 读取目录
    File[] files = FileUtils.readDirectoryListing(baseDir);
    // 判断目录是否为空
    if (null == files) return ResponseEntity.ok("");
    
    // 表示合并后的目录
    String mergePublicDir = fullProperties.getPublicDir() + File.separator + filename;
    // 判断是否合并成功
    boolean mergeFlag;
    try {
      mergeFlag = FileUtils.mergeFile(files, mergePublicDir);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    // 如果 merge成功后 删除临时文件
    if (mergeFlag)
      mergeFlag = FileUtils.deleteIfExists(Paths.get(baseDir).toString());
    
    return ResponseEntity.ok(mergeFlag);
  }
  
  @Override
  public int partMerge(int readIdx, String filename) {
    // 转换的path
    // 其中 filename 是带有文件后缀的 filename
    String covertPath = FileUtils.joinPath(fullProperties.getConvertDir(), filename + "-" + readIdx);
    
    // 原则上 merge的时候 应该有可以merge的东西
    File[] files = FileUtils.readDirectoryListing(FileUtils.joinPath(fullProperties.getTmpDir(), FileUtils.getNameExcludeExt(filename)));
    if (null == files) return -1;
    int newReadIdx = files.length;
    
    // 如果长度是一致的话，说明没有东西可以获取
    if (readIdx == newReadIdx) return readIdx;
    
    // 切出了 部分files
    File[] cutFiles = Arrays.copyOfRange(files, readIdx, files.length);
    File covertFile = new File(covertPath);
    
    // 表示 新的cut files
    File[] newCutFiles = new File[cutFiles.length + (covertFile.isFile() ? 1 : 0)];
    if (covertFile.isFile()) {
      newCutFiles[0] = covertFile;
      System.arraycopy(cutFiles, 0, newCutFiles, 1, cutFiles.length);
    } else {
      newCutFiles = cutFiles;
    }
    
    // 构建新的文件
    try {
      FileUtils.mergeFile(newCutFiles, FileUtils.joinPath(fullProperties.getConvertDir(), filename + "-" + newReadIdx));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    // 删除之前合并的文件
    FileUtils.deleteIfExists(covertPath);
    return newReadIdx;
  }
}
