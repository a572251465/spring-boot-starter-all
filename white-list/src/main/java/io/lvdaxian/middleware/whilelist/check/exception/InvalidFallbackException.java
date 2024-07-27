package io.lvdaxian.middleware.whilelist.check.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidFallbackException extends Exception {
  public InvalidFallbackException(String message) {
    super(message);
  }
  
  public InvalidFallbackException(String message, Throwable cause) {
    super(message, cause);
  }
}
