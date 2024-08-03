package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.strategy.SelectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这是 切片文件上传的 策略实现
 *
 * @author lihh
 */
public class SectionStrategyImpl implements SelectStrategy {
  @Override
  public void accept(HttpServletRequest req, HttpServletResponse res, FileOperate fileOperate) {
    System.out.println("section");
  }
}
