package io.lvdaxian.upload.file.strategy.impl.adapter;

import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.strategy.SelectStrategyFactory;
import io.lvdaxian.upload.file.strategy.factory.DefaultSelectStrategyFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认 选择策略适配器
 *
 * @author lihh
 */
public class DefaultSelectStrategyAdapter implements SelectStrategyFactory {
  private SelectStrategyFactory factory;
  // 单例模式
  public static SelectStrategyFactory INSTANCE = new DefaultSelectStrategyAdapter(DefaultSelectStrategyFactory.INSTANCE);
  
  private DefaultSelectStrategyAdapter() {
  }
  
  public DefaultSelectStrategyAdapter(SelectStrategyFactory factory) {
    this.factory = factory;
    DefaultSelectStrategyFactory.INSTANCE.register();
  }
  
  @Override
  public SelectStrategy newSelectStrategy(HttpServletRequest req) {
    return this.factory.newSelectStrategy(req);
  }
}
