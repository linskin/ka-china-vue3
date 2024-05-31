package com.qyt.project.model.vo.category;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类返回简单实体
 *
 * @author Linskin
 * @date 2023/6/14
 */
@Data
public class CategorySimpleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

}