package io.lvdaxian.upload.file.thread.utils;

import io.lvdaxian.upload.file.thread.entity.ThreadTask;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConstVariable {
  // 表示 并发锁 也是为了抢占任务
  public final static ReentrantLock CONST_CONCURRENCY_LOCK = new ReentrantLock();
  // 表示 等待队列
  public final static Queue<String> waitQueue = new ArrayBlockingQueue<>(100);
  // 表示操作的 thread 信息
  public final static ConcurrentMap<String, ThreadTask> threadTaskMap = new ConcurrentHashMap<>();
  // 表示计算的最大线程数
  public final static int maxThreadCount = Math.max(2, Runtime.getRuntime().availableProcessors());
  // 表示 使用的线程个数
  public final static AtomicInteger usedThreadCount = new AtomicInteger(0);
  // 记录 id 以及 thread 缓存, 其实为了打断线程用的
  public final static ConcurrentMap<String, Thread> idAndThreadMap = new ConcurrentHashMap<>();
  // 表示 是否快速启动
  public final static ConcurrentMap<String, Boolean> quickStartMapCache = new ConcurrentHashMap<>();
  // 表示 基础 sleep 时间, 以 秒 为单位, 尽可能的拉长时间 这样的话 可能减少IO操作
  public final static int BASE_THREAD_SLEEP_TIME = 5;
}