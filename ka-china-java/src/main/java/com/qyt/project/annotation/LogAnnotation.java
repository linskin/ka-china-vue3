package com.qyt.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAnnotation {

    /**
     * 模块标题
     *
     * @return
     */
    String title() default "";

    /**
     * 是否保存日志
     *
     * @return
     */
    boolean isSaveLog() default true;
}
