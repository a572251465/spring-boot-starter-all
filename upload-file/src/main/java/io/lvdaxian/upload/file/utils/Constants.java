package io.lvdaxian.upload.file.utils;

public class Constants {
  public static class REQUEST_URL {
    public final static String SECTION_REQUEST_URL = "/upload/section/([A-Za-z0-9_]+)/([A-Za-z0-9_]+.[A-Za-z0-9]+\\-\\d)";
    public final static String VERIFY_REQUEST_URL = "/upload/verify/([A-Za-z0-9]+.[A-Za-z0-9]+)";
    public final static String LIST_REQUEST_URL = "/upload/list/([A-Za-z0-9]+)";
    public final static String MERGE_REQUEST_URL = "/upload/merge/([A-Za-z0-9]+)/([A-Za-z0-9]+.[A-Za-z0-9]+)";
  }
  
  // 表示保存的临时目录
  public final static String fileSaveTmpDir = "uploadFileTmpDir";
  public final static String ENABLED_TYPE_DISK = "disk";
}