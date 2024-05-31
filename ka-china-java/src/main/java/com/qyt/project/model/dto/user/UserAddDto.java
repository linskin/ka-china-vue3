package com.qyt.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户添加实体
 *
 * @author Linskin
 * @date 2023/6/11
 */
@Data
public class UserAddDto implements Serializable {

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
     * 用户角色: user, admin
     */
    private String role;
}
