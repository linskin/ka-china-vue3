package com.qyt.project.model.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子创建实体
 *
 * @author Linskin
 * @date 2023/6/15
 */
@Data
public class PostAddDto implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 描述
     */
    private String description;

    /**
     * 内容
     */
    private String content;

    /**
     * 分类ID
     */
    private List<Long> categoryIds;

}
