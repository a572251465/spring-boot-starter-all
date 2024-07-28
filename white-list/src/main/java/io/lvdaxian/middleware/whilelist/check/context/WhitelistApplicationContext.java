package io.lvdaxian.middleware.whilelist.check.context;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Getter
@Component("whitelistContext")
public class WhitelistApplicationContext implements ApplicationContextAware {
  
  private ApplicationContext context;
  
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }
}
