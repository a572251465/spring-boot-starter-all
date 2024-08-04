package io.lvdaxian.upload.file.interceptor;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.strategy.impl.adapter.DefaultSelectStrategyAdapter;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 从 这里 拦截 http 请求
 *
 * @author lihh
 */
@Component
public class HttpRequestInterceptor implements HandlerInterceptor {
  
  @Resource
  private FileOperate fileOperate;
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 拿到执行策略
    SelectStrategy selectStrategy = DefaultSelectStrategyAdapter.INSTANCE.newSelectStrategy(request);
    if (null == selectStrategy)
      return HandlerInterceptor.super.preHandle(request, response, handler);
    
    // 从这里拿到策略开始执行
    ResponseEntity entity = selectStrategy.accept(request, fileOperate);
    // 设置响应的类型
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    
    // socket 往回写数据
    PrintWriter out = response.getWriter();
    out.write(entity.toString());
    out.flush();
    
    return false;
  }
}
