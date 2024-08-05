package io.lvdaxian.upload.file.expose.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import io.lvdaxian.upload.file.entity.UploadFileProperties;
import io.lvdaxian.upload.file.exception.UploadFileNotFoundException;
import io.lvdaxian.upload.file.expose.UploadFileUtils;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.Constants;
import io.lvdaxian.upload.file.utils.FileUtils;
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
  private UploadFileProperties properties;
  
  /**
   * 将 file 转换为 MultipartFile
   *
   * @param file    待转换的文件
   * @param newName 修改文件的名称
   * @return 返回的MultipartFile
   * @author lihh
   */
  @Override
  public MultipartFile convertFileToMultipartFile(File file, String newName) {
    if (null == newName)
      newName = file.getName();
    
    DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem(newName, ContentType.MULTIPART.getValue(), true, file.getName());
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
   * @param fileName 文件名称
   * @return 返回 MultipartFile
   * @author lihh
   */
  @Override
  public MultipartFile getMultipartFileByName(String fileName) {
    if (!StrUtil.equals(properties.getEnabledType(), Constants.ENABLED_TYPE_DISK))
      return null;
    
    // 如果能执行到这里，说明一定创建了目录
    String publicDir = CommonUtils.getAbsolutePath(FileUtils.getFileRealPath(properties.getSaveDir())) + File.separator + "public",
        filePath = publicDir + File.separator + fileName;
    
    File file = new File(filePath);
    if (!file.exists() || !file.isFile()) {
      String message = String.format("upload file[%s] not found, in dir[%s]", fileName, publicDir);
      log.error(message);
      throw new UploadFileNotFoundException(message);
    }
    
    // 将 file 进行转换
    return convertFileToMultipartFile(file, fileName);
  }
}
