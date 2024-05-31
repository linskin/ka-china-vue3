package com.qyt.project.model.dto.banner;

import com.qyt.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * banner查询实体
 *
 * @author Linskin
 * @date 2023/6/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BannerQueryDto extends PageRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 名称
     */
    private String name;

}
