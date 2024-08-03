package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.strategy.SelectStrategy;

import javax.servlet.http.HttpServletResponse;

/**
 * 这是 切片文件上传的 策略实现
 *
 * @author lihh
 */
public class SectionStrategyImpl implements SelectStrategy {
  @Override
  public void accept(HttpServletResponse httpServletResponse) {
    System.out.println("section");
  }
}
