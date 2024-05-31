package com.qyt.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.mapper.PostCategoryMapper;
import com.qyt.project.model.entity.PostCategory;
import com.qyt.project.service.PostCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子_分类服务实现
 *
 * @author Linskin
 * @date 2023/6/15
 */
@Slf4j
@Service
public class PostCategoryServiceImpl extends ServiceImpl<PostCategoryMapper, PostCategory> implements PostCategoryService {

    @Resource
    private PostCategoryMapper postCategoryMapper;

    @Override
    public void addPostCategory(Long postId, List<Long> categoryIds) {
        List<PostCategory> postCategoryList = categoryIds.stream().map(categoryId -> {
            PostCategory postCategory = new PostCategory();
            postCategory.setPostId(postId);
            postCategory.setCategoryId(categoryId);
            return postCategory;
        }).collect(Collectors.toList());
        boolean save = this.saveBatch(postCategoryList);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "帖子分类创建失败，数据库错误");
        }
    }

    @Override
    public void deletePostCategory(Long postId) {
        QueryWrapper<PostCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("postId", postId);
        postCategoryMapper.delete(queryWrapper);
    }

    @Override
    public List<Long> getCategoryIds(Long postId) {
        QueryWrapper<PostCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("postId", postId);
        List<PostCategory> postCategories = postCategoryMapper.selectList(queryWrapper);
        List<Long> categoryIds = new ArrayList<>();
        for (PostCategory postCategory : postCategories) {
            Long categoryId = postCategory.getCategoryId();
            categoryIds.add(categoryId);
        }
        return categoryIds;
    }
}
