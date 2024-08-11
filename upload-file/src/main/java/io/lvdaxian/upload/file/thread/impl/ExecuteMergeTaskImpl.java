package io.lvdaxian.upload.file.thread.impl;

import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.notify.NotifyCenter;
import io.lvdaxian.upload.file.notify.entity.Event;
import io.lvdaxian.upload.file.notify.enumeration.PublisherTypeEnum;
import io.lvdaxian.upload.file.thread.entity.ThreadTask;
import io.lvdaxian.upload.file.thread.enumeration.ThreadMergeEnum;
import io.lvdaxian.upload.file.thread.utils.CommonUtils;
import io.lvdaxian.upload.file.thread.utils.ConstVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

public class ExecuteMergeTaskImpl implements Runnable {
  
  private static final Logger log = LoggerFactory.getLogger(ExecuteMergeTaskImpl.class);
  private final String id;
  private final FileOperate fileOperate;
  
  public ExecuteMergeTaskImpl(String id, FileOperate fileOperate) {
    this.id = id;
    this.fileOperate = fileOperate;
  }
  
  public void executeNextTask() {
    try {
      ConstVariable.CONST_CONCURRENCY_LOCK.lock();
      
      Queue<String> waitQueue = ConstVariable.waitQueue;
      if (waitQueue.isEmpty()) return;
      
      String nextTaskId = ConstVariable.waitQueue.remove();
      //  进行下一个任务
      NotifyCenter.publishEvent(PublisherTypeEnum.nextTask, Event.builder().id(nextTaskId).build());
    } finally {
      ConstVariable.CONST_CONCURRENCY_LOCK.unlock();
    }
  }
  
  /**
   * 正常结束 or 打断线程的 接下来的处理
   *
   * @param isInterrupt 是否打断线程
   * @author lihh
   */
  public void normalEndAndInterruptThreadNextProcess(boolean isInterrupt) {
    ThreadTask currentTask = ConstVariable.threadTaskMap.get(id);
    
    // 判断 线程是否被打断
    if (isInterrupt) {
      try {
        ConstVariable.CONST_CONCURRENCY_LOCK.lock();
        
        // 线程使用递减，有可能出现线程饥饿问题
        // 递减 线程个数的时候，多个线程同时抢占 && 执行
        ConstVariable.usedThreadCount.decrementAndGet();
        executeNextTask();
        return;
      } finally {
        ConstVariable.CONST_CONCURRENCY_LOCK.unlock();
      }
    }
    
    // 有可能异步的情况，线程睡眠一段时间后，文件已经合并完成了
    if (
        null == currentTask ||
            currentTask.getMergeType() == ThreadMergeEnum.COMPLETE
    ) {
      Thread thread = ConstVariable.idAndThreadMap.get(id);
      if (null != thread)
        // 标记 线程中断
        thread.interrupt();
      return;
    }
    
    // 已经读到的索引
    int readIdx = currentTask.getReadIndex();
    // 开始 复制部分 临时文件
    int newReadIdx = this.fileOperate.partMerge(readIdx, this.id);
    currentTask.setReadIndex(newReadIdx);
    
    if (-1 == newReadIdx) {
      Thread thread = ConstVariable.idAndThreadMap.get(id);
      if (null != thread)
        thread.interrupt();
    }
  }
  
  @Override
  public void run() {
    log.info(String.format(io.lvdaxian.upload.file.utils.CommonUtils.getCommonPrefixAndSuffix("id/thread ~ %s/%s"), id, Thread.currentThread().getName()));
    
    try {
      for (; ; ) {
//        Thread.sleep(CommonUtils.calculateThreadSleepTime());
        Thread.sleep(500);
        
        // 正常 or 异常结束执行处理
        normalEndAndInterruptThreadNextProcess(false);
      }
    } catch (InterruptedException e) {
      normalEndAndInterruptThreadNextProcess(true);
    }
  }
}
