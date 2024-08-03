package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.strategy.SelectStrategy;

import javax.servlet.http.HttpServletResponse;

/**
 * 验证 文件是否存在
 *
 * @author lihh
 */
public class VerifyStrategyImpl implements SelectStrategy {
  @Override
  public void accept(HttpServletResponse httpServletResponse) {
    System.out.println("verify");
  }
}
