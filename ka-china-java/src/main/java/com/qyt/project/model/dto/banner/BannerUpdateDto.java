package com.qyt.project.model.dto.banner;

import lombok.Data;

import java.io.Serializable;

/**
 * banner更新实体
 *
 * @author Linskin
 * @date 2023/6/13
 */
@Data
public class BannerUpdateDto implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 封面
     */
    private String cover;

}
