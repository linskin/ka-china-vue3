package com.qyt.project.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户返回简单实体
 *
 * @author Linskin
 * @date 2023/6/11
 */
@Data
public class UserSimpleVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号
     */
    private String account;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户角色: user, admin
     */
    private String role;
}
