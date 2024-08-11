package io.lvdaxian.upload.file.notify.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PublisherTypeEnum {
  firingTask("firingTask"),
  nextTask("nextTask");
  private String type;
}
