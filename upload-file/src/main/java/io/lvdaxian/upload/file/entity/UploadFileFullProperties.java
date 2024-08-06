package io.lvdaxian.upload.file.entity;

import cn.hutool.core.util.StrUtil;
import io.lvdaxian.upload.file.exception.ParamErrorException;
import io.lvdaxian.upload.file.utils.Constants;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class UploadFileFullProperties {
  
  private String baseDir;
  private String tmpDir;
  private String publicDir;
  private final UploadFileProperties innerProperties;
  
  public UploadFileFullProperties(UploadFileProperties properties) {
    this.innerProperties = properties;
    
    String[] types = new String[]{Constants.ENABLED_TYPE_MINO, Constants.ENABLED_TYPE_DISK};
    if (!Arrays.asList(types).contains(properties.getEnabledType()))
      throw new ParamErrorException(String.format("params[io.lvdaxian.upload.file.enabled-type] it can only be %s", String.join(" or ", types)));
    
    // 只有是 disk 的时候 才有意义
    if (StrUtil.equals(Constants.ENABLED_TYPE_DISK, this.innerProperties.getEnabledType())) {
      if (StrUtil.isEmpty(properties.getSaveDir()))
        this.innerProperties.setSaveDir(Constants.fileSaveTmpDir);
      else {
        String saveDir = properties.getSaveDir();
        if (properties.getSaveDir().startsWith("./"))
          saveDir = properties.getSaveDir().substring(2, saveDir.length());
        this.innerProperties.setSaveDir(saveDir);
      }
    }
    if (0 == properties.getHttpInterceptorOrder())
      this.innerProperties.setHttpInterceptorOrder(Constants.DEFAULT_INTERCEPTOR_ORDER);
  }
  
  public String setBaseDir(String baseDir) {
    return this.baseDir = baseDir;
  }
  
  public String setTmpDir(String tmpDir) {
    return this.tmpDir = tmpDir;
  }
  
  public String setPublicDir(String publicDir) {
    return this.publicDir = publicDir;
  }
}
