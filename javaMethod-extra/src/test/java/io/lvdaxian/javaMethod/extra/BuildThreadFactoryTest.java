package io.lvdaxian.javaMethod.extra;

import io.lvdaxian.javaMethod.extra.thread.factory.impl.BuildThreadFactory;
import org.junit.Test;

public class BuildThreadFactoryTest {
  
  @Test
  public void test01() {
    BuildThreadFactory threadFactory = BuildThreadFactory.builder().prefix("self").build();
    
    Thread currThread = threadFactory.newThread(() -> {
      System.out.println(Thread.currentThread().getName());
    });
    
    currThread.start();
  }
  
  @Test
  public void test03() {
    BuildThreadFactory.builder().clazz(BuildThreadFactoryTest.class).build().newThread(() -> {
      System.out.println(Thread.currentThread().getName());
    }).start();
  }
  
  @Test
  public void test02() {
    Thread t = new Thread(() -> {
      System.out.println(Thread.currentThread().getName());
    });
    t.start();
  }
}
