package com.qyt.project.annotation;

import com.qyt.project.constant.UserConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 *
 * @author Linskin
 */


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 有任何一个角色
     *
     * @return
     */
    String[] anyRole() default {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE, UserConstant.DEFAULT_ROLE};

    /**
     * 必须有某个角色
     *
     * @return
     */
//    String mustRole() default "";

}

