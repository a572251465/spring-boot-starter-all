package io.lvdaxian.upload.file.exception;

public class ParamErrorException extends RuntimeException {
  public ParamErrorException() {
  }
  
  public ParamErrorException(String message) {
    super(message);
  }
}
