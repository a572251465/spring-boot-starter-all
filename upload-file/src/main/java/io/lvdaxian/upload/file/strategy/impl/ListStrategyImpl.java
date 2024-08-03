package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.strategy.SelectStrategy;

import javax.servlet.http.HttpServletResponse;

/**
 * 查询 上传文件的列表
 *
 * @author lihh
 */
public class ListStrategyImpl implements SelectStrategy {
  @Override
  public void accept(HttpServletResponse httpServletResponse) {
    System.out.println("list");
  }
}
