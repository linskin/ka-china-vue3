package com.qyt.project.model.dto.post;

import com.qyt.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 帖子查询实体
 *
 * @author Linskin
 * @date 2023/6/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryDto extends PageRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 标题
     */
    private String title;

}
