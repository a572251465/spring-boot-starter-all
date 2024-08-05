package io.lvdaxian.upload.file.exception;

public class ParamCountException extends RuntimeException {
  public ParamCountException() {
  }
  
  public ParamCountException(String message) {
    super(message);
  }
}
