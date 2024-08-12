package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.exception.ParamCountException;
import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.Constants;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 查询 上传文件的列表
 *
 * @author lihh
 */
public class ListStrategyImpl implements SelectStrategy {
  
  @Override
  public ResponseEntity accept(HttpServletRequest req, FileOperate fileOperate) {
    String requestURI = req.getRequestURI();
    List<Object> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.LIST_REQUEST_URL);
    
    if (null == list || list.size() != 1)
      throw new ParamCountException(String.format("request url[%s] wrong number of parameters", Constants.REQUEST_URL.LIST_REQUEST_URL));
    
    return fileOperate.list((String) list.get(0));
  }
}
