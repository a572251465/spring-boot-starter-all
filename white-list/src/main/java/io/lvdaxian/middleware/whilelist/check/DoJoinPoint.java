package io.lvdaxian.middleware.whilelist.check;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 切面 逻辑实现
 *
 * @author lihh
 */
@Component
@Aspect
public class DoJoinPoint {
  
  @Resource
  private String whileListConfig;
  
  /**
   * 定义 拦截的注解
   *
   * @author lihh
   */
  @Pointcut("@annotation(io.lvdaxian.middleware.whilelist.check.annotation.DoWhiteList)")
  public void aopPoint() {
  }
  
  /**
   * 根据 反射点 拿到方法
   *
   * @param jp 切入点
   * @return 反射拿到的方法
   * @author lihh
   */
  private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
    Signature sig = jp.getSignature();
    MethodSignature methodSignature = (MethodSignature) sig;
    return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
  }
}
