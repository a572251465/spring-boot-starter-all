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
 * 文件 merge 策略实现
 *
 * @author lihh
 */
public class MergeStrategyImpl implements SelectStrategy {
  
  @Override
  public ResponseEntity accept(HttpServletRequest req, FileOperate fileOperate) {
    String requestURI = req.getRequestURI();
    List<Object> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.MERGE_REQUEST_URL);
    
    if (null == list || list.size() != 2)
      throw new ParamCountException(String.format("request url[%s] wrong number of parameters", Constants.REQUEST_URL.MERGE_REQUEST_URL));
    
    return fileOperate.merge((String) list.get(0), (String) list.get(1));
  }
}
