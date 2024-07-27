package io.lvdaxian.middleware.whilelist.check;

import io.lvdaxian.middleware.whilelist.check.annotation.DoWhiteList;
import io.lvdaxian.middleware.whilelist.check.exception.InvalidFallbackException;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 切面 逻辑实现
 *
 * @author lihh
 */
@Component
@Aspect
public class DoJoinPoint {
  
  private static final Logger log = LoggerFactory.getLogger(DoJoinPoint.class);
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
   * 该方法 添加 around 注解
   *
   * @param jp 切入点
   * @return 返回对象
   * @author lihh
   */
  @Around("aopPoint()")
  public Object doRouter(ProceedingJoinPoint jp) throws Throwable {
    // 获取方法
    Method method = getMethod(jp);
    // 根据方法 获取注解信息
    DoWhiteList whiteList = method.getAnnotation(DoWhiteList.class);
    
    // 获取字段值
    // filterKey 白名单过滤的key
    // jp.getArgs() 拿到的返回值
    String filterValue = getFieldValue(whiteList.filterKey(), jp.getArgs());
    log.info("middleware whitelist handler method：{} value：{}", method.getName(), filterValue);
    
    if (null == filterValue || filterValue.isEmpty()) return jp.proceed();
    
    // 过滤名称的 value值
    String[] filterNameValues = whileListConfig.split(",");
    
    for (String name : filterNameValues)
      if (filterValue.equals(name))
        return jp.proceed();
    
    // 拦截
    return returnObject(whiteList, method);
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
  
  /**
   * 获取 返回对象
   *
   * @param whiteList 白名单注解
   * @param method    执行的方法
   * @return 返回的对象值
   * @author lihh
   */
  private Object returnObject(DoWhiteList whiteList, Method method) throws InstantiationException, IllegalAccessException, InvalidFallbackException {
    // 返回类型
    Class<?> returnType = method.getReturnType();
    // 表示 降级的对象
    Class<?> fallbackClass = whiteList.fallback();
    
    // 判断 是否实现了Supplier
    if (!Supplier.class.isAssignableFrom(fallbackClass))
      throw new InvalidFallbackException("the fallback class must be implemented by " + Supplier.class.getName());
    
    // 通过类 拿到实例
    Object instance = fallbackClass.newInstance();
    Object returnObj = ((Supplier<?>) instance).get();
    // 如果为空的话 直接实例化对象
    if (null == returnObj || "".equals(returnObj))
      return returnType.newInstance();
    
    return returnObj;
  }
  
  /**
   * 根据字段名称  以及参数args 获取具体的值
   *
   * @param fieldName 字段名称
   * @param args      参数
   * @return 返回的值
   * @author lihh
   */
  private String getFieldValue(String fieldName, Object[] args) {
    String fieldValue = null;
    
    for (Object arg : args) {
      try {
        if (null == fieldValue || fieldValue.isEmpty()) {
          fieldValue = BeanUtils.getProperty(arg, fieldName);
        } else break;
      } catch (Exception e) {
        if (args.length == 1) return args[0].toString();
      }
    }
    
    return fieldValue;
  }
}
