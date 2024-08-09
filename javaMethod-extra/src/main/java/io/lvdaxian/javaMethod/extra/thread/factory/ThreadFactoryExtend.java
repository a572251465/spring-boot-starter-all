package io.lvdaxian.javaMethod.extra.thread.factory;

import java.util.concurrent.ThreadFactory;

public interface ThreadFactoryExtend extends ThreadFactory {
  Thread newThread(Runnable r, String threadName);
  
  Thread newThread(Runnable r, String prefix, String threadName);
}
