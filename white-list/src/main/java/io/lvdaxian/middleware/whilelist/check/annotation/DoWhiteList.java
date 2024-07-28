package io.lvdaxian.middleware.whilelist.check.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 运行时保留策略表示注解会被保留到编译后的字节码文件中，
 * 并且可以在运行时通过反射等方式获取到这些注解信息。
 * 这种保留策略通常用于自定义注解，以便在运行时处理这些注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoWhiteList {
  String filterKey() default "";
  
  // 默认是白名单 反之 是黑名单
  boolean isWhiteList() default true;
  
  // 降级的方法
  Class<?> fallback() default Void.class;
}
