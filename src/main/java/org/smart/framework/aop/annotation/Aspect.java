package org.smart.framework.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect{
    /**
     * 包名  代理包名下的所有类
     * @return
     */
    String pkg() default "";

    /**
     * 类名  ,代理此类
     */
    String cls() default "";

    /**
     * 注解, 代理有此注解的包名下的类
     */
    Class<? extends Annotation> annotation() default Aspect.class;

}
