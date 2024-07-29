package io.lvdaxian.middleware.whitelist.check.exception;

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
