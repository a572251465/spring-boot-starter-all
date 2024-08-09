package io.lvdaxian.javaMethod.extra.thread.factory.impl;

import cn.hutool.core.util.StrUtil;
import io.lvdaxian.javaMethod.extra.thread.factory.ThreadFactoryExtend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildThreadFactory implements ThreadFactoryExtend {
  
  private final AtomicInteger nextId = new AtomicInteger();
  private String prefix;
  private int priority;
  private boolean daemon;
  private Class<?> clazz;
  
  
  /**
   * 构建 thread name
   *
   * @param threadName 线程名称
   * @return 返回新的线程名称
   * @author lihh
   */
  public String generateThreadName(String threadName) {
    threadName = StrUtil.isEmpty(threadName) ? "" : "-" + threadName;
    
    Class<?> localClass = clazz;
    if (null == localClass)
      localClass = this.getClass();
    
    String localPrefix = this.prefix;
    if (StrUtil.isEmpty(localPrefix))
      localPrefix = localClass.getSimpleName();
    
    return localPrefix + "-Thread-" + nextId.getAndIncrement() + threadName;
  }
  
  @Override
  public Thread newThread(Runnable r) {
    return newThread(r, null);
  }
  
  @Override
  public Thread newThread(Runnable r, String threadName) {
    return newThread(r, threadName, null);
  }
  
  @Override
  public Thread newThread(Runnable r, String prefix, String threadName) {
    if (StrUtil.isNotEmpty(prefix))
      this.prefix = prefix;
    
    final String newThreadName = generateThreadName(threadName);
    
    if (0 == priority)
      priority = 1;
    
    // 优先级 判断设置
    if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
      throw new IllegalArgumentException(
          "priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
    }
    
    Thread t = new Thread(r, newThreadName);
    t.setPriority(priority);
    t.setDaemon(daemon);
    
    return t;
  }
}
