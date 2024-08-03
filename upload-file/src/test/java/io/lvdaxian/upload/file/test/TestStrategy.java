package io.lvdaxian.upload.file.test;

import io.lvdaxian.upload.file.strategy.SelectStrategy;
import io.lvdaxian.upload.file.strategy.impl.adapter.DefaultSelectStrategyAdapter;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class TestStrategy {
  
  @Test
  public void test01() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("GET");
    request.setRequestURI("/upload/verify/aaabb");
    
    SelectStrategy selectStrategy = DefaultSelectStrategyAdapter.INSTANCE.newSelectStrategy(request);
    selectStrategy.accept(null, null, null);
  }
  
  @Test
  public void test02() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("POST");
    request.setRequestURI("/upload/section/aaabb/dsdf11.txt_1");
    
    SelectStrategy selectStrategy = DefaultSelectStrategyAdapter.INSTANCE.newSelectStrategy(request);
    selectStrategy.accept(null, null, null);
  }
  
  @Test
  public void test03() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setMethod("GET");
    request.setRequestURI("/upload/test/aa");
    
    SelectStrategy selectStrategy = DefaultSelectStrategyAdapter.INSTANCE.newSelectStrategy(request);
    selectStrategy.accept(null, null, null);
  }
}
