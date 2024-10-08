package io.lvdaxian.upload.file.strategy.factory;

import cn.hutool.core.util.StrUtil;
import io.lvdaxian.upload.file.enumeration.RequestEnum;
import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.strategy.SelectStrategyFactory;
import io.lvdaxian.upload.file.strategy.entity.RequestMatchEntity;
import io.lvdaxian.upload.file.strategy.impl.ListStrategyImpl;
import io.lvdaxian.upload.file.strategy.impl.MergeStrategyImpl;
import io.lvdaxian.upload.file.strategy.impl.SectionStrategyImpl;
import io.lvdaxian.upload.file.strategy.impl.VerifyStrategyImpl;
import io.lvdaxian.upload.file.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表示 默认实现的 选择策略工厂
 *
 * @author lihh
 */
public class DefaultSelectStrategyFactory extends AbstractSelectStrategyFactory implements SelectStrategyFactory {
  
  // 表示 默认的工厂实例
  public static DefaultSelectStrategyFactory INSTANCE = new DefaultSelectStrategyFactory();
  
  /**
   * 注册 策略factory
   *
   * @author lihh
   */
  public void register() {
    // 表示 文件名称 正则
    INSTANCE.register(
        RequestMatchEntity.builder()
            .method(RequestEnum.POST)
            .isMatch(true)
            .requestUrl(Constants.REQUEST_URL.SECTION_REQUEST_URL)
            .build(),
        new SectionStrategyImpl()
    );
    
    INSTANCE.register(
        RequestMatchEntity.builder()
            .isMatch(true)
            .method(RequestEnum.GET)
            .requestUrl(Constants.REQUEST_URL.VERIFY_REQUEST_URL)
            .build(),
        new VerifyStrategyImpl()
    );
    
    INSTANCE.register(
        RequestMatchEntity.builder()
            .isMatch(true)
            .method(RequestEnum.GET)
            .requestUrl(Constants.REQUEST_URL.LIST_REQUEST_URL)
            .build(),
        new ListStrategyImpl()
    );
    
    INSTANCE.register(
        RequestMatchEntity.builder()
            .isMatch(true)
            .method(RequestEnum.GET)
            .requestUrl(Constants.REQUEST_URL.MERGE_REQUEST_URL)
            .build(),
        new MergeStrategyImpl()
    );
  }
  
  private DefaultSelectStrategyFactory() {
  }
  
  @Override
  public SelectStrategy newSelectStrategy(HttpServletRequest req) {
    String method = req.getMethod(), reqURL = req.getRequestURI();
    
    for (Map.Entry<RequestMatchEntity, SelectStrategy> map : strategyMapping.entrySet()) {
      
      // 解构 对象变量
      RequestMatchEntity entity = map.getKey();
      String requestURL = entity.getRequestUrl();
      RequestEnum requestEnum = entity.getMethod();
      boolean isMatch = entity.isMatch();
      
      // 先 匹配 method
      if (!StrUtil.equals(method.toLowerCase(), requestEnum.getMethod().toLowerCase()))
        continue;
      
      // 能执行到这里，最起码请求方法是相同的
      if (!isMatch) {
        if ((requestURL.hashCode() ^ reqURL.hashCode()) == 0)
          return map.getValue();
      } else {
        // requestURL 这是正则 URL
        Pattern pattern = Pattern.compile(requestURL);
        Matcher matcher = pattern.matcher(reqURL);
        if (matcher.find()) {
          return map.getValue();
        }
      }
    }
    return null;
  }
}
