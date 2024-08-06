package io.lvdaxian.upload.file.config.interceptor;

import cn.hutool.core.util.StrUtil;
import io.lvdaxian.upload.file.entity.UploadFileFullProperties;
import io.lvdaxian.upload.file.interceptor.HttpRequestInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration("aliasInterceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer {
  
  @Resource
  private UploadFileFullProperties fullProperties;
  
  @Bean
  public HttpRequestInterceptor createRequestInterceptor() {
    return new HttpRequestInterceptor();
  }
  
  @Override
  public void addInterceptors(@NotNull InterceptorRegistry registry) {
    int httpInterceptorOrder = fullProperties.getInnerProperties().getHttpInterceptorOrder();
    String contextPrefix = fullProperties.getInnerProperties().getContextPrefix();
    
    if (StrUtil.isNotEmpty(contextPrefix)) {
      if (!contextPrefix.startsWith("/"))
        contextPrefix = "/" + contextPrefix;
      if (contextPrefix.endsWith("/"))
        contextPrefix = contextPrefix.substring(0, contextPrefix.length() - 1);
    }
    
    registry.addInterceptor(createRequestInterceptor()).addPathPatterns(contextPrefix + "/upload/**").order(httpInterceptorOrder);
  }
}
