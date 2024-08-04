package io.lvdaxian.upload.file.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
  /**
   * 解析 rest 请求的 params
   *
   * @param requestURL 访问的url
   * @param regxURL    正则匹配的url
   * @return 返回拿到集合
   */
  public static List<Object> resolveRestParams(String requestURL, String regxURL) {
    Pattern pattern = Pattern.compile(regxURL);
    Matcher matcher = pattern.matcher(requestURL);
    if (matcher.find()) {
      List<Object> objectSet = new ArrayList<>();
      for (int i = 1; i <= matcher.groupCount(); i += 1)
        objectSet.add(matcher.group(i));
      return objectSet.isEmpty() ? null : objectSet;
    }
    return null;
  }
  
  /**
   * 从 request 中获取MultipartFile
   *
   * @param req http request 请求
   * @return 返回 MultipartFile
   * @author lihh
   */
  public static MultipartFile getMultipartFileByRequest(HttpServletRequest req) {
    try {
      MultipartHttpServletRequest servletRequest = (StandardMultipartHttpServletRequest) req;
      return servletRequest.getFile("file");
    } catch (Exception e) {
      return null;
    }
  }
}
