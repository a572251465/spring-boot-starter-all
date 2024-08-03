package io.lvdaxian.upload.file.interceptor;

import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.strategy.impl.adapter.DefaultSelectStrategyAdapter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 从 这里 拦截 http 请求
 *
 * @author lihh
 */
public class HttpRequestInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 拿到执行策略
    SelectStrategy selectStrategy = DefaultSelectStrategyAdapter.INSTANCE.newSelectStrategy(request);
    if (null == selectStrategy)
      return HandlerInterceptor.super.preHandle(request, response, handler);
    
    // 从这里拿到策略开始执行
    selectStrategy.accept(request, response, null);
    return false;
  }
}
