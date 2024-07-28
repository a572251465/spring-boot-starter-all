package io.lvdaxian.middleware.whilelist.check.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GlobalUniqueFallbackException extends Exception {
  public GlobalUniqueFallbackException(String message) {
    super(message);
  }
  
  public GlobalUniqueFallbackException(String message, Throwable cause) {
    super(message, cause);
  }
}
