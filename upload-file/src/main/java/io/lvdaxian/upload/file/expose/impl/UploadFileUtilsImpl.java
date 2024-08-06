package io.lvdaxian.upload.file.expose.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import io.lvdaxian.upload.file.entity.UploadFileFullProperties;
import io.lvdaxian.upload.file.exception.UploadFileNotFoundException;
import io.lvdaxian.upload.file.expose.UploadFileUtils;
import io.lvdaxian.upload.file.utils.Constants;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件上传 工具包的实现类
 *
 * @author lihh
 */
@Component
public class UploadFileUtilsImpl implements UploadFileUtils {
  
  private static final Logger log = LoggerFactory.getLogger(UploadFileUtilsImpl.class);
  @Resource
  private UploadFileFullProperties fullProperties;
  
  /**
   * 将 file 转换为 MultipartFile
   *
   * @param file     待转换的文件
   * @param filename 修改文件的名称
   * @return 返回的MultipartFile
   * @author lihh
   */
  @Override
  public MultipartFile convertFileToMultipartFile(File file, String filename) {
    if (null == filename) filename = file.getName();
    
    DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem(filename, ContentType.MULTIPART.getValue(), true, file.getName());
    try {
      Files.copy(Paths.get(file.getAbsolutePath()), fileItem.getOutputStream());
      return new CommonsMultipartFile(fileItem);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public MultipartFile convertFileToMultipartFile(File file) {
    return convertFileToMultipartFile(file, null);
  }
  
  /**
   * 通过 fileName 拿到 MultipartFile
   *
   * @param newFilename 新的文件名称
   * @param oldFilename 旧文件名称
   * @return 返回 MultipartFile
   * @author lihh
   */
  @Override
  public MultipartFile getMultipartFileByName(String newFilename, String oldFilename) {
    // 将 file 进行转换
    return convertFileToMultipartFile(getFileByName(newFilename), oldFilename);
  }
  
  /**
   * 通过 单个filename 拿到 MultipartFile
   *
   * @param newFilename 文件名称
   * @return 返回 MultipartFile
   * @author lihh
   */
  @Override
  public MultipartFile getMultipartFileByName(String newFilename) {
    return getMultipartFileByName(newFilename, null);
  }
  
  /**
   * 根据 文件名称 获取文件
   *
   * @param fileName 文件名称
   * @return 返回的文件
   * @author lihh
   */
  @Override
  public File getFileByName(String fileName) {
    if (!StrUtil.equals(fullProperties.getInnerProperties().getEnabledType(), Constants.ENABLED_TYPE_DISK))
      return null;
    
    // 如果能执行到这里，说明一定创建了目录
    String filePath = fullProperties.getPublicDir() + File.separator + fileName;
    
    File file = new File(filePath);
    if (!file.exists() || !file.isFile()) {
      String message = String.format("upload file[%s] not found, in dir[%s]", fileName, fullProperties.getPublicDir());
      log.error(message);
      throw new UploadFileNotFoundException(message);
    }
    
    return file;
  }
  
  /**
   * 根据 filename 拿到 file 对应 path string
   *
   * @param fileName 文件名称
   * @return 返回 file path string
   * @author lihh
   */
  @Override
  public String getPathString(String fileName) {
    return getPath(fileName).toString();
  }
  
  /**
   * 根据 文件名称 拿到 Path
   *
   * @param newFilename 文件名称
   * @return 返回 file path
   * @author lihh
   */
  @Override
  public Path getPath(String newFilename) {
    return getFileByName(newFilename).toPath();
  }
}
