package com.qyt.project.model.dto.category;

import com.qyt.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 分类查询实体
 *
 * @author Linskin
 * @date 2023/6/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryQueryDto extends PageRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 名称
     */
    private String name;

}