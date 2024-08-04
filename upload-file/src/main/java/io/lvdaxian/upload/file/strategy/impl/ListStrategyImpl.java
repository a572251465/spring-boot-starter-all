package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 查询 上传文件的列表
 *
 * @author lihh
 */
public class ListStrategyImpl implements SelectStrategy {
  @Override
  public ResponseEntity accept(HttpServletRequest req, FileOperate fileOperate) {
    return null;
  }
}
