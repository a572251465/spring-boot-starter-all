package io.lvdaxian.middleware.hystrix;

import io.lvdaxian.middleware.hystrix.annotation.DoHystrix;
import io.lvdaxian.middleware.hystrix.valve.IValveService;
import io.lvdaxian.middleware.hystrix.valve.impl.HystrixValveImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 定义 一个切面类
 *
 * @author lihh
 */
@Component
@Aspect
public class DoHystrixPoint {
  
  // 对注解进行切面
  @Pointcut("@annotation(io.lvdaxian.middleware.hystrix.annotation.DoHystrix)")
  public void aopPoint() {
  }
  
  // @Around 是环绕 切面通知
  // @Around("aopPoint()") 必须作用在 被 aopPoint 所修饰的方法
  // @annotation(doGovern) 只有被 doGovern 注解标记的方法才会被这个环绕通知所影响
  @Around("aopPoint() && @annotation(doGovern)")
  public Object doRouter(ProceedingJoinPoint jp, DoHystrix doGovern) throws Throwable {
    IValveService valveService = new HystrixValveImpl();
    return valveService.access(jp, getMethod(jp), doGovern, jp.getArgs());
  }
  
  private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
    Signature sig = jp.getSignature();
    MethodSignature methodSignature = (MethodSignature) sig;
    return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
  }
}
