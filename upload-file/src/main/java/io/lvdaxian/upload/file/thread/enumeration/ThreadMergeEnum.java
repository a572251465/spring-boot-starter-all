package io.lvdaxian.upload.file.thread.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ThreadMergeEnum {
  COMPLETE("COMPLETE"),
  MERGE("MERGE"),
  NotStarted("NotStarted");
  private final String type;
}
