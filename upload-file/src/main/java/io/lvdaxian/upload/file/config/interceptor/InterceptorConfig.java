package io.lvdaxian.upload.file.config.interceptor;

import io.lvdaxian.upload.file.entity.UploadFileProperties;
import io.lvdaxian.upload.file.interceptor.HttpRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  
  @Resource
  private UploadFileProperties properties;
  
  @Bean
  public HttpRequestInterceptor createRequestInterceptor() {
    return new HttpRequestInterceptor();
  }
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(createRequestInterceptor()).addPathPatterns("/upload/**").order(properties.getHttpInterceptorOrder());
  }
}
