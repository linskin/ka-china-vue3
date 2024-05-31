package com.qyt.project.model.dto.user;

import com.qyt.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询实体
 *
 * @author Linskin
 * @date 2023/6/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDto extends PageRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号
     */
    private String account;

}
