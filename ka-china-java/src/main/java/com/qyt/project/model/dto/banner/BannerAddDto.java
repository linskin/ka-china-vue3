package com.qyt.project.model.dto.banner;

import lombok.Data;

import java.io.Serializable;

/**
 * banner添加实体
 *
 * @author Linskin
 * @date 2023/6/13
 */
@Data
public class BannerAddDto implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 名称
     */
    private String name;

    /**
     * 封面
     */
    private String cover;
}
