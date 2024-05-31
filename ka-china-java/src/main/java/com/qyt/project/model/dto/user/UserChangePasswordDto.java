package com.qyt.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改密码实体
 *
 * @author Linskin
 * @date 2023/6/12
 */
@Data
public class UserChangePasswordDto implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String reNewPassword;

}
