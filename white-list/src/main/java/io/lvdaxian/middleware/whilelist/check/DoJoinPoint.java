package io.lvdaxian.middleware.whilelist.check;

import io.lvdaxian.middleware.whilelist.check.annotation.DoWhiteList;
import io.lvdaxian.middleware.whilelist.check.context.WhitelistApplicationContext;
import io.lvdaxian.middleware.whilelist.check.exception.GlobalUniqueFallbackException;
import io.lvdaxian.middleware.whilelist.check.exception.NotFoundFallbackException;
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

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 切面 逻辑实现
 *
 * @author lihh
 */
@Aspect
public class DoJoinPoint {
  
  private static final Logger log = LoggerFactory.getLogger(DoJoinPoint.class);
  @Resource
  private String whileListConfig;
  @Resource(name = "whitelistContext")
  private WhitelistApplicationContext whitelistContext;
  
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
    // 判断是 白名单 还是 黑名单, 白名单 默认是 false
    boolean isWhiteList = whiteList.isWhiteList();
    
    boolean assignValueExist = false;
    for (String name : filterNameValues)
      if (filterValue.equals(name)) {
        assignValueExist = true;
        break;
      }
    
    // isWhiteList == true && assignValueExist == true 表示白名单，指定的值存在 表示可以执行下一步
    // isWhiteList == false && assignValueExist == false 表示黑名单，指定的值不存在，表示可以执行下一步
    if (isWhiteList) {
      if (assignValueExist) return jp.proceed();
    } else {
      if (!assignValueExist) return jp.proceed();
    }
    
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
   * 通过 接口从bean中 获取实现类
   *
   * @return 返回 Class
   * @author lihh
   */
  private Class<?> getClassByInterface() throws GlobalUniqueFallbackException {
    String[] beanNames = whitelistContext.getContext().getBeanNamesForType(FallbackProcessor.class);
    // 表示 全局实现有多个实现 FallbackProcessor 的类
    if (beanNames.length > 1)
      throw new GlobalUniqueFallbackException("Global FallbackProcessor.class instantiated bean is not unique");
    if (beanNames.length == 0)
      return null;
    
    return whitelistContext.getContext().getBean(beanNames[0], FallbackProcessor.class).getClass();
  }
  
  /**
   * 获取 返回对象
   *
   * @param whiteList 白名单注解
   * @param method    执行的方法
   * @return 返回的对象值
   * @author lihh
   */
  private Object returnObject(DoWhiteList whiteList, Method method) throws InstantiationException, IllegalAccessException, GlobalUniqueFallbackException, NotFoundFallbackException {
    // 返回类型
    Class<?> returnType = method.getReturnType();
    // 表示 降级的对象  首先从注解中获取
    Class<?> fallbackClass = whiteList.fallback();
    // 判断实现类  是否实现接口
    if (!FallbackProcessor.class.isAssignableFrom(fallbackClass))
      // 从全局中 拿
      fallbackClass = getClassByInterface();
    
    if (fallbackClass == null)
      throw new NotFoundFallbackException("local or global must be implemented FallbackProcessor.class");
    
    // 通过类 拿到实例
    Object instance = fallbackClass.newInstance();
    Object returnObj = ((FallbackProcessor<?>) instance).handle();
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
