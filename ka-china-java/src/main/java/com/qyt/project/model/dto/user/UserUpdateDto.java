package com.qyt.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新实体
 *
 * @author Linskin
 * @date 2023/6/11
 */
@Data
public class UserUpdateDto implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

}
