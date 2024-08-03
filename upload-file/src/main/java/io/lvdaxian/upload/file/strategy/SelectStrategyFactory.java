package io.lvdaxian.upload.file.strategy;

import javax.servlet.http.HttpServletRequest;

public interface SelectStrategyFactory {
  SelectStrategy newSelectStrategy(HttpServletRequest req);
}
