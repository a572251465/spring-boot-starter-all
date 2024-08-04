package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件 merge 策略实现
 *
 * @author lihh
 */
public class MergeStrategyImpl implements SelectStrategy {
  @Override
  public ResponseEntity accept(HttpServletRequest req, FileOperate fileOperate) {
    System.out.println("merge");
    return null;
  }
}
