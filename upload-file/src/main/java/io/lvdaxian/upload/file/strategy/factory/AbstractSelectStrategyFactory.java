package io.lvdaxian.upload.file.strategy.factory;

import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.strategy.entity.RequestMatchEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象 选择策略工厂
 *
 * @author lihh
 */
abstract class AbstractSelectStrategyFactory {
  protected final Map<RequestMatchEntity, SelectStrategy> strategyMapping = new HashMap<>();
  
  protected void register(RequestMatchEntity k, SelectStrategy v) {
    strategyMapping.put(k, v);
  }
}
