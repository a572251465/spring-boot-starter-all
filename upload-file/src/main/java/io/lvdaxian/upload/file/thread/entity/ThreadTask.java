package io.lvdaxian.upload.file.thread.entity;

import io.lvdaxian.upload.file.thread.enumeration.ThreadMergeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ThreadTask implements Serializable {
  private String id;
  private long firstFiringTime;
  private ThreadMergeEnum mergeType;
  // 表示 读取文件的索引
  private int readIndex;
}
