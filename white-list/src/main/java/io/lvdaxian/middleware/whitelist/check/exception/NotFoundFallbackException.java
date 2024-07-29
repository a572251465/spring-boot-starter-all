package io.lvdaxian.middleware.whitelist.check.exception;

public class NotFoundFallbackException extends Exception {
  public NotFoundFallbackException(String message) {
    super(message);
  }
  
  public NotFoundFallbackException(String message, Throwable cause) {
    super(message, cause);
  }
}
