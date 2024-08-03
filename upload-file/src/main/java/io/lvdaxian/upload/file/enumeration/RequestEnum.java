package io.lvdaxian.upload.file.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestEnum {
  GET("GET"),
  DELETE("DELETE"),
  PATCH("PATCH"),
  POST("POST");
  
  private final String method;
}
