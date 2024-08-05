package io.lvdaxian.upload.file.strategy.impl;

import io.lvdaxian.upload.file.exception.ParamCountException;
import io.lvdaxian.upload.file.exception.UploadFileException;
import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.Constants;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 这是 切片文件上传的 策略实现
 *
 * @author lihh
 */
public class SectionStrategyImpl implements SelectStrategy {
  @Override
  public ResponseEntity accept(HttpServletRequest req, FileOperate fileOperate) {
    String requestURI = req.getRequestURI();
    List<Object> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.SECTION_REQUEST_URL);
    if (null == list || list.size() != 2)
      throw new ParamCountException(String.format("request url[%s] wrong number of parameters", Constants.REQUEST_URL.SECTION_REQUEST_URL));
    
    // 从这里获取 file
    MultipartFile file = CommonUtils.getMultipartFileByRequest(req);
    if (null == file)
      throw new UploadFileException("upload file is null or error in type, eg: file=null");
    
    return fileOperate.upload(file, (String) list.get(0), (String) list.get(1));
  }
}
