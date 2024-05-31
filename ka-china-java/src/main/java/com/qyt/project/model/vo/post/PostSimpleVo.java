package com.qyt.project.model.vo.post;

import com.qyt.project.model.vo.category.CategorySimpleVo;
import com.qyt.project.model.vo.user.UserSimpleVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子返回简单实体
 *
 * @author Linskin
 * @date 2023/6/15
 */
@Data
public class PostSimpleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

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
     * 分类信息
     */
    private List<CategorySimpleVo> categories;

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
