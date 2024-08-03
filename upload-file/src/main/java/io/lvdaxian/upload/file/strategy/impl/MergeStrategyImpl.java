package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.strategy.SelectStrategy;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件 merge 策略实现
 *
 * @author lihh
 */
public class MergeStrategyImpl implements SelectStrategy {
  @Override
  public void accept(HttpServletResponse httpServletResponse) {
    System.out.println("merge");
  }
}
