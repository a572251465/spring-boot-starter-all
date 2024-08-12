package io.lvdaxian.upload.file.lifecycle;

import cn.hutool.core.util.StrUtil;
import io.lvdaxian.upload.file.entity.UploadFileFullProperties;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.Constants;
import io.lvdaxian.upload.file.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static io.lvdaxian.upload.file.utils.CommonUtils.getCommonPrefixAndSuffix;

/**
 * 容器 刷新结束 钩子
 *
 * @author lihh
 */
@Component
public class ContainerRefreshDone implements ApplicationRunner {
  
  private static final Logger log = LoggerFactory.getLogger(ContainerRefreshDone.class);
  @Resource
  private UploadFileFullProperties fullProperties;
  
  @Override
  public void run(ApplicationArguments args) throws Exception {
    // 拿到变量
    String contextPrefix = fullProperties.getInnerProperties().getContextPrefix(),
        enabledType = fullProperties.getInnerProperties().getEnabledType();
    int httpInterceptorOrder = fullProperties.getInnerProperties().getHttpInterceptorOrder();
    int delayConcurrencyMergeTime = fullProperties.getInnerProperties().getDelayConcurrencyMergeTime();
    int threadSleepTime = fullProperties.getInnerProperties().getThreadSleepTime();
    
    log.info(getCommonPrefixAndSuffix("startup successful"));
    log.info(getCommonPrefixAndSuffix(String.format("contextPrefix/ %s", CommonUtils.getValueOrDefault(contextPrefix, "-"))));
    log.info(getCommonPrefixAndSuffix(String.format("enabledType/ %s", enabledType)));
    log.info(getCommonPrefixAndSuffix(String.format("httpInterceptorOrder/ %s", httpInterceptorOrder)));
    log.info(getCommonPrefixAndSuffix(String.format("delayConcurrencyMergeTime/ %s", delayConcurrencyMergeTime)));
    log.info(getCommonPrefixAndSuffix(String.format("threadSleepTime/ %s", threadSleepTime)));
    
    if (StrUtil.equals(enabledType, Constants.ENABLED_TYPE_DISK)) {
      log.info(getCommonPrefixAndSuffix(String.format("saveDir/ %s", fullProperties.getBaseDir())));
      FileUtils.deleteDirectoryListing(FileUtils.joinPath(fullProperties.getConvertDir()));
    }
  }
}
