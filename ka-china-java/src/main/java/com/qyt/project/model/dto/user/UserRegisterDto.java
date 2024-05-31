package com.qyt.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册实体
 *
 * @author Linskin
 * @date 2023/6/11
 */
@Data
public class UserRegisterDto implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String rePassword;

}
