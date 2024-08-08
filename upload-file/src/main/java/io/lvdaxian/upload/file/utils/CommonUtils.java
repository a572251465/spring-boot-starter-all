package io.lvdaxian.upload.file.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
   * 根据 相对路径 获取 绝对路径
   *
   * @param relativePath 相对路径
   * @return 绝对路径
   * @author lihh
   */
  public static Path getAbsolutePath(String relativePath) {
    Path path = Paths.get(relativePath);
    return path.toAbsolutePath();
  }
  
  /**
   * 拿到 共同的前缀 以及后缀
   *
   * @param str 传递的字符串
   * @author lihh
   */
  public static String getCommonPrefixAndSuffix(String str) {
    return String.format(" >>> upload file jdk: %s <<< ", str);
  }
  
  /***
   * 表示解析的参数 从而返回字符串
   *
   * @author lihh
   * @param str 打印的字符串
   * @return 返回拼接字符串
   */
  public static String resolveRequestPathString(String str) {
    return String.format("resolve request params: %s", str);
  }
  
  /**
   * 判断 str 是否为空，反之使用替换 str
   *
   * @param value        获取的值
   * @param replaceValue 替换的值
   * @return 最终的字符串
   * @author lihh
   */
  public static String getValueOrDefault(String value, String replaceValue) {
    return StrUtil.isEmpty(value) ? replaceValue : value;
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
