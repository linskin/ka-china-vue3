package com.qyt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qyt.project.model.entity.PostCategory;

import java.util.List;

/**
 * 帖子_分类服务
 *
 * @author Linskin
 * @date 2023/6/15
 */
public interface PostCategoryService extends IService<PostCategory> {

    /**
     * 添加帖子分类
     *
     * @param postId 帖子ID
     * @param categoryIds 分类ID集合
     * @return 状态
     */
    void addPostCategory(Long postId, List<Long> categoryIds);

    /**
     * 删除帖子分类
     *
     * @param postId 帖子ID
     * @return 状态
     */
    void deletePostCategory(Long postId);

    /**
     * 获取该帖子的所有分类ID
     *
     * @param postId 帖子ID
     * @return 分类ID集合
     */
    List<Long> getCategoryIds(Long postId);

}
