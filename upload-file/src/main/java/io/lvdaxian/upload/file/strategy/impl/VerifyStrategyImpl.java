package io.lvdaxian.upload.file.strategy.impl;

import com.alibaba.fastjson2.JSON;
import io.lvdaxian.upload.file.exception.ParamCountException;
import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.Constants;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 验证 文件是否存在
 *
 * @author lihh
 */
public class VerifyStrategyImpl implements SelectStrategy {
  private static final Logger log = LoggerFactory.getLogger(VerifyStrategyImpl.class);
  
  @Override
  public ResponseEntity accept(HttpServletRequest req, FileOperate fileOperate) {
    String requestURI = req.getRequestURI();
    List<Object> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.VERIFY_REQUEST_URL);
    log.info(CommonUtils.getCommonPrefixAndSuffix(CommonUtils.resolveRequestPathString(JSON.toJSONString(list))));
    
    if (null == list || list.size() != 1)
      throw new ParamCountException(String.format("request url[%s] wrong number of parameters", Constants.REQUEST_URL.VERIFY_REQUEST_URL));
    
    return fileOperate.verify((String) list.get(0));
  }
}
