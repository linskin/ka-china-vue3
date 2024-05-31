package com.qyt.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子_分类实体
 *
 * @author Linskin
 * @date 2023/6/15
 */
@TableName(value = "post_category")
@Data
public class PostCategory implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
