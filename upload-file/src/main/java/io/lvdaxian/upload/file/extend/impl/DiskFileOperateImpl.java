package io.lvdaxian.upload.file.extend.impl;

import io.lvdaxian.upload.file.entity.UploadFileFullProperties;
import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.notify.NotifyCenter;
import io.lvdaxian.upload.file.notify.entity.Event;
import io.lvdaxian.upload.file.notify.enumeration.PublisherTypeEnum;
import io.lvdaxian.upload.file.thread.entity.ThreadTask;
import io.lvdaxian.upload.file.thread.enumeration.ThreadMergeEnum;
import io.lvdaxian.upload.file.thread.utils.ConstVariable;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.Constants;
import io.lvdaxian.upload.file.utils.FileUtils;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "io.lvdaxian.upload.file.enabled-type", havingValue = "disk")
public class DiskFileOperateImpl implements FileOperate {
  
  private static final Logger log = LoggerFactory.getLogger(DiskFileOperateImpl.class);
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
    return ResponseEntity.ok(FileUtils.isFileExist(FileUtils.joinPath(fullProperties.getPublicDir(), filename)));
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
    
    // 断点续传的方式下 清空中间状态
    if (list.length > 0) {
      String id = list[0].split("-")[0];
      
      ThreadTask threadTask = ConstVariable.threadTaskMap.get(id);
      if (null != threadTask)
        mergeSuccessNextHandler(id, false);
      
      // 标记 快速开始
      ConstVariable.quickStartMapCache.put(id, true);
    }
    
    return ResponseEntity.ok(Arrays.asList(list.length, fileSize));
  }
  
  /**
   * 表示 merge 的扩展功能
   *
   * @param id 指的是文件名
   * @author lihh
   */
  public boolean mergeExtend(String id) {
    ThreadTask threadTask = ConstVariable.threadTaskMap.get(id);
    // 异常 情况, 有可能是线程饥饿导致的（一直等待中，而切片文件已经上传成功了）
    if (null == threadTask) return false;
    
    ThreadMergeEnum mergeType = threadTask.getMergeType();
    int readIndex = threadTask.getReadIndex();
    
    // 如果读取到的索引是 -1 的话，属于非正常情况
    // 如果thread状态 是MERGE的话，正常运行中
    // 如果thread状态 是NotStarted的话，还在时间计算过程中
    if (
        !Arrays.asList(ThreadMergeEnum.MERGE, ThreadMergeEnum.NotStarted).contains(mergeType) ||
            -1 == readIndex
    ) {
      log.info(CommonUtils.getCommonPrefixAndSuffix(String.format("%s merge success, finish state is %s", id, mergeType.getType())));
      return false;
    }
    
    String filenameExcludeExt = FileUtils.getNameExcludeExt(id);
    // 如果能执行到这里，最起码是按照正常流程执行的
    File[] files = FileUtils.readDirectoryListing(
        FileUtils.joinPath(fullProperties.getTmpDir(), filenameExcludeExt)
    );
    if (null == files) return false;
    
    // 满足此条件的话，说明最后还没收尾，再次调用后进行收尾
    if (readIndex != files.length) {
      readIndex = partMerge(readIndex, id);
      threadTask.setMergeCount(threadTask.getMergeCount() + 1);
    }
    threadTask.setReadIndex(readIndex);
    
    // 再次读了一次，读了最后一次 还不到的话，说明是未知的异常情况
    if (readIndex != files.length)
      return false;
    
    // 文件复制
    String covertPath = FileUtils.joinPath(fullProperties.getConvertDir(), id + "-" + readIndex);
    String publicPath = FileUtils.joinPath(fullProperties.getPublicDir(), id);
    
    if (!FileUtils.isFileExist(covertPath))
      return false;
    try {
      Files.copy(Path.of(covertPath), Path.of(publicPath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    // 复制成功后 删除原来的文件
    FileUtils.deleteIfExists(covertPath);
    log.info(CommonUtils.getCommonPrefixAndSuffix(String.format("%s merge success, merge count is: %s", id, threadTask.getMergeCount())));
    return true;
  }
  
  /**
   * merge 成功后的处理
   *
   * @param id              文件名称
   * @param isDeleteTmpFile 是否临时文件
   * @author lihh
   */
  public void mergeSuccessNextHandler(String id, boolean isDeleteTmpFile) {
    ThreadTask threadTask = ConstVariable.threadTaskMap.get(id);
    ConstVariable.threadTaskMap.remove(id);
    ConstVariable.quickStartMapCache.remove(id);
    // 将等待队列中 元素 删除
    if (!ConstVariable.waitQueue.isEmpty())
      ConstVariable.waitQueue.remove(id);
    
    // 打断线程
    Thread thread = ConstVariable.idAndThreadMap.get(id);
    if (null != thread)
      thread.interrupt();
    ConstVariable.idAndThreadMap.remove(id);
    
    if (isDeleteTmpFile)
      FileUtils.deleteIfExists(FileUtils.joinPath(fullProperties.getTmpDir(), FileUtils.getNameExcludeExt(id)));
    if (null != threadTask) {
      int readIndex = threadTask.getReadIndex();
      FileUtils.deleteIfExists(FileUtils.joinPath(fullProperties.getConvertDir(), id + "-" + readIndex));
    }
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
    baseDir = FileUtils.joinPath(fullProperties.getTmpDir(), baseDir);
    // 读取目录
    File[] files = FileUtils.readDirectoryListing(baseDir);
    // 判断目录是否为空
    if (null == files) return ResponseEntity.ok("");
    
    // 进行 merge extend
    if (mergeExtend(filename)) {
      mergeSuccessNextHandler(filename, true);
      return ResponseEntity.ok(true);
    }
    
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
      FileUtils.deleteIfExists(Paths.get(baseDir).toString());
    
    // 清空状态
    if (ConstVariable.waitQueue.contains(filename))
      log.info(CommonUtils.getCommonPrefixAndSuffix(String.format("%s in wait queue, auto merge", filename)));
    mergeSuccessNextHandler(filename, true);
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
