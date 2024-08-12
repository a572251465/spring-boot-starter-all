package io.lvdaxian.upload.file.extend;

import io.lvdaxian.upload.file.thread.entity.ThreadTask;
import io.lvdaxian.upload.file.thread.utils.ConstVariable;
import io.lvdaxian.upload.file.utils.result.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileOperate {
  ResponseEntity upload(MultipartFile file, String baseDir, String filename);
  
  ResponseEntity verify(String filename);
  
  ResponseEntity list(String baseDir);
  
  ResponseEntity merge(String baseDir, String filename);
  
  int partMerge(int readIdx, String filename);
  
  /**
   * merge 成功后 清空状态
   *
   * @param id 文件名称
   * @author lihh
   */
  default ThreadTask mergeSuccessCleanStatus(String id) {
    ThreadTask threadTask = ConstVariable.threadTaskMap.get(id);
    ConstVariable.threadTaskMap.remove(id);
    ConstVariable.quickStartMapCache.remove(id);
    // 将等待队列中 元素 删除
    if (!ConstVariable.waitQueue.isEmpty())
      ConstVariable.waitQueue.remove(id);
    
    // 打断线程
    Thread thread = ConstVariable.idAndThreadMap.get(id);
    if (null != thread)
      thread.interrupt();
    ConstVariable.idAndThreadMap.remove(id);
    
    return threadTask;
  }
}
