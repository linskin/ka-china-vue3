package com.qyt.project.model.vo.category;

import com.qyt.project.model.vo.user.UserSimpleVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分类返回实体
 *
 * @author Linskin
 * @date 2023/6/14
 */
@Data
public class CategoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建用户
     */
    private UserSimpleVo userInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}