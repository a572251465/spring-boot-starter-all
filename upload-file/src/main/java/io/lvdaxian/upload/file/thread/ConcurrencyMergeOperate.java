package io.lvdaxian.upload.file.thread;

public interface ConcurrencyMergeOperate {
  void firingTask(String id);
  boolean isMergeComplete(String id);
}
