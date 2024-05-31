package com.qyt.project.model.vo;

import com.qyt.project.model.vo.user.UserSimpleVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * banner返回实体
 *
 * @author Linskin
 * @date 2023/6/13
 */
@Data
public class BannerVo implements Serializable {

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
     * 封面
     */
    private String cover;

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
