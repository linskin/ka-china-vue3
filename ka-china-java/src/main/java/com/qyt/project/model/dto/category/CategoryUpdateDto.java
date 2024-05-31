package com.qyt.project.model.dto.category;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类更新实体
 *
 * @author Linskin
 * @date 2023/6/14
 */
@Data
public class CategoryUpdateDto implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

}
