package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证 文件是否存在
 *
 * @author lihh
 */
public class VerifyStrategyImpl implements SelectStrategy {
  @Override
  public ResponseEntity accept(HttpServletRequest req, FileOperate fileOperate) {
    System.out.println("verify");
    return null;
  }
}
