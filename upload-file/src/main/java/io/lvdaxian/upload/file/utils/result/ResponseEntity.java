package io.lvdaxian.upload.file.utils.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseEntity implements Serializable {
  private boolean success;
  private Object data;
  private String code = "200";
  private String message;
  
  public static ResponseEntity ok(Object data) {
    return ResponseEntity.builder().success(true).data(data).code("200").build();
  }
}
