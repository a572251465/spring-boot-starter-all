package io.lvdaxian.upload.file.strategy.entity;

import io.lvdaxian.upload.file.enumeration.RequestEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestMatchEntity implements Serializable {
  private String requestUrl;
  private RequestEnum method;
  private boolean isMatch;
}
