package io.lvdaxian.upload.file.entity;

import cn.hutool.core.util.StrUtil;
import io.lvdaxian.upload.file.exception.ParamErrorException;
import io.lvdaxian.upload.file.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public class UploadFileFullProperties {
  
  private String baseDir;
  private String tmpDir;
  private String convertDir;
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
    
    if (StrUtil.isEmpty(properties.getContextPrefix()))
      this.innerProperties.setContextPrefix("");
    
    // 设置 延迟并发合并 的时间
    // 如果时间 <= -1 的话 都设置为1-
    // 如果为0的话 直接设置默认值
    if (this.innerProperties.getDelayConcurrencyMergeTime() <= Constants.CONST_QUICKLY_DELAY_CONCURRENCY_MERGE_TIME)
      this.innerProperties.setDelayConcurrencyMergeTime(Constants.CONST_QUICKLY_DELAY_CONCURRENCY_MERGE_TIME);
    if (0 == this.innerProperties.getDelayConcurrencyMergeTime())
      this.innerProperties.setDelayConcurrencyMergeTime(Constants.DEFAULT_DELAY_CONCURRENCY_MERGE_TIME);
  }
  
  public String setBaseDir(String baseDir) {
    return this.baseDir = baseDir;
  }
  
  public String setTmpDir(String tmpDir) {
    return this.tmpDir = tmpDir;
  }
  
  public String setConvertDir(String convertDir) {
    return this.convertDir = convertDir;
  }
  
  public String setPublicDir(String publicDir) {
    return this.publicDir = publicDir;
  }
}
