package io.lvdaxian.upload.file.thread.utils;

/**
 * 线程 线程公共工具包
 *
 * @author lihh
 */
public class CommonUtils {
  
  /**
   * 计算线程的睡眠时间
   *
   * @return 返回睡眠的时间
   * @author lihh
   */
  public static int calculateThreadSleepTime(int baseSleepTime) {
    return (baseSleepTime + (int) (ConstVariable.usedThreadCount.get() * 0.5)) * 1000;
  }
}