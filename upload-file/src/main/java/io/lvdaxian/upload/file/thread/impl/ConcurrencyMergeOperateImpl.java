package io.lvdaxian.upload.file.thread.impl;

import io.lvdaxian.upload.file.entity.UploadFileFullProperties;
import io.lvdaxian.upload.file.extend.FileOperate;
import io.lvdaxian.upload.file.notify.NotifyCenter;
import io.lvdaxian.upload.file.notify.entity.Event;
import io.lvdaxian.upload.file.notify.enumeration.PublisherTypeEnum;
import io.lvdaxian.upload.file.notify.listener.Subscriber;
import io.lvdaxian.upload.file.thread.ConcurrencyMergeOperate;
import io.lvdaxian.upload.file.thread.entity.ThreadTask;
import io.lvdaxian.upload.file.thread.enumeration.ThreadMergeEnum;
import io.lvdaxian.upload.file.thread.factory.impl.BuildThreadFactory;
import io.lvdaxian.upload.file.thread.utils.ConstVariable;
import io.lvdaxian.upload.file.utils.CommonUtils;
import io.lvdaxian.upload.file.utils.Constants;
import io.lvdaxian.upload.file.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 并发合集 操作实现类
 *
 * @author lihh
 */
@Component
public class ConcurrencyMergeOperateImpl implements ConcurrencyMergeOperate {
  
  private static final Logger log = LoggerFactory.getLogger(ConcurrencyMergeOperateImpl.class);
  @Resource
  private UploadFileFullProperties fullProperties;
  @Resource
  private FileOperate fileOperate;
  
  @PostConstruct
  public void initAfter() {
    ConcurrencyMergeOperate _this = this;
    
    // 实现 订阅事件
    Subscriber subscriberEvent = new Subscriber() {
      @Override
      public void onEvent(Event event) {
        _this.firingTask(event.getId());
      }
    };
    NotifyCenter.registerSubscriber(PublisherTypeEnum.nextTask, subscriberEvent);
    NotifyCenter.registerSubscriber(PublisherTypeEnum.firingTask, subscriberEvent);
  }
  
  private void executeTask(String id) {
    ThreadTask currentTask = ConstVariable.threadTaskMap.get(id);
    if (null == currentTask) {
      log.error(CommonUtils.getCommonPrefixAndSuffix(String.format("merge task[%s] is null", id)));
      return;
    }
    
    // 判断是否具备执行的资格
    ThreadMergeEnum mergeType = currentTask.getMergeType();
    if (
        ThreadMergeEnum.NotStarted == mergeType ||
            ThreadMergeEnum.COMPLETE == mergeType
    )
      return;
    
    // 判断是否合并完成
    if (FileUtils.isFileExist(FileUtils.joinPath(fullProperties.getPublicDir(), id))) {
      currentTask.setMergeType(ThreadMergeEnum.COMPLETE);
      return;
    }
    
    // 判断是否有线程还可以执行, 如果已经满的话，直接添加到等待队列中
    if (ConstVariable.maxThreadCount == ConstVariable.usedThreadCount.get()) {
      try {
        ConstVariable.CONST_CONCURRENCY_LOCK.lock();
        // 双重检测 避免并发
        if (ConstVariable.maxThreadCount == ConstVariable.usedThreadCount.get())
          ConstVariable.waitQueue.add(id);
      } finally {
        ConstVariable.CONST_CONCURRENCY_LOCK.unlock();
      }
      return;
    }
    
    // 使用线程执行任务
    BuildThreadFactory threadFactory = BuildThreadFactory.builder().prefix("merge task").build();
    // 线程个数 添加
    ConstVariable.usedThreadCount.getAndIncrement();
    Thread thread = threadFactory.newThread(new ExecuteMergeTaskImpl(id, fileOperate));
    ConstVariable.idAndThreadMap.put(id, thread);
    thread.start();
  }
  
  @Override
  public void firingTask(String id) {
    ThreadTask currentTask = ConstVariable.threadTaskMap.get(id);
    
    // 如果没有task的话 肯定是第一次执行
    if (null == currentTask) {
      // 基础任务
      ThreadTask baseTask = ThreadTask.builder()
          .id(id)
          .firstFiringTime(System.currentTimeMillis())
          .mergeType(ThreadMergeEnum.NotStarted)
          .build();
      
      // 是否立马开始
      boolean isFastStart = Constants.CONST_QUICKLY_DELAY_CONCURRENCY_MERGE_TIME == fullProperties.getInnerProperties().getDelayConcurrencyMergeTime();
      if (isFastStart)
        baseTask.setMergeType(ThreadMergeEnum.MERGE);
      
      // 添加缓存
      ConstVariable.threadTaskMap.put(id, baseTask);
      executeTask(id);
      return;
    }
    
    // 判断是否已经执行
    ThreadMergeEnum mergeType = currentTask.getMergeType();
    if (
        mergeType == ThreadMergeEnum.MERGE ||
            mergeType == ThreadMergeEnum.COMPLETE
    ) return;
    
    // 表示当时间
    long currentTime = System.currentTimeMillis(), firstFiringTime = currentTask.getFirstFiringTime();
    int delayTime = fullProperties.getInnerProperties().getDelayConcurrencyMergeTime();
    
    // 时间不足 不足以启动
    if (currentTime - firstFiringTime < (delayTime * 1000L)) return;
    
    currentTask.setMergeType(ThreadMergeEnum.MERGE);
    executeTask(id);
  }
  
  @Override
  public boolean isMergeComplete(String id) {
    return false;
  }
}
