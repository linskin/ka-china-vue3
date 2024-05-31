package com.qyt.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.constant.UserConstant;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.mapper.PostMapper;
import com.qyt.project.model.dto.post.PostAddDto;
import com.qyt.project.model.dto.post.PostUpdateDto;
import com.qyt.project.model.entity.Post;
import com.qyt.project.model.vo.user.UserVo;
import com.qyt.project.service.PostCategoryService;
import com.qyt.project.service.PostService;
import com.qyt.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 帖子服务实现
 *
 * @author Linskin
 * @date 2023/6/15
 */
@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private UserService userService;

    @Resource
    private PostCategoryService postCategoryService;

    public void validData(String title, String description, String content, List<Long> categoryIds) {
        if (StringUtils.isAnyBlank(title, description, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数不完整");
        }
        if (StringUtils.isNotBlank(title) && (title.length() < 2 || title.length() > 40)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "标题长度需在2-40之间");
        }
        if (StringUtils.isNotBlank(description) && (description.length() < 10 || description.length() > 500)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "描述长度需在10-500之间");
        }
        if (categoryIds.size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请至少选择一个分类");
        }
    }

    @Override
    public long addPost(PostAddDto postAddDto, Long userId) {
        String title = postAddDto.getTitle();
        String cover = postAddDto.getCover();
        String description = postAddDto.getDescription();
        String content = postAddDto.getContent();
        List<Long> categoryIds = postAddDto.getCategoryIds();
        validData(title, description, content, categoryIds);
        Post post = new Post();
        post.setTitle(title);
        post.setCover(cover);
        post.setDescription(description);
        post.setContent(content);
        post.setUserId(userId);
        int insert = postMapper.insert(post);
        if (insert <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建失败，数据库错误");
        }
        postCategoryService.addPostCategory(post.getId(), categoryIds);
        return post.getId();
    }

    @Override
    public boolean deletePost(Long postId, UserVo currentUser) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败，未找到该帖子");
        }
        UserVo userVo = userService.getUserById(post.getUserId());
        if (userVo != null && !(UserConstant.SUPER_ADMIN.equals(currentUser.getRole()) || UserConstant.ADMIN_ROLE.equals(currentUser.getRole())) && !currentUser.getId().equals(userVo.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "普通用户只能删除自己的帖子");
        }
        int delete = postMapper.deleteById(postId);
        if (delete > 0) {
            postCategoryService.deletePostCategory(postId);
        }
        return delete > 0;
    }

    @Override
    public boolean deletePostBatch(List<Long> postIds, UserVo currentUser) {
        int delete = postMapper.deleteBatchIds(postIds);
        return delete > 0;
    }

    @Override
    public boolean updatePost(PostUpdateDto postUpdateDto) {
        Long id = postUpdateDto.getId();
        String title = postUpdateDto.getTitle();
        String cover = postUpdateDto.getCover();
        String description = postUpdateDto.getDescription();
        String content = postUpdateDto.getContent();
        List<Long> categoryIds = postUpdateDto.getCategoryIds();
        if (id == null || id == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未收到帖子ID");
        }
        validData(title, description, content, categoryIds);
        UpdateWrapper<Post> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("title", title);
        updateWrapper.set("cover", cover);
        updateWrapper.set("description", description);
        updateWrapper.set("content", content);
        int update = postMapper.update(new Post(), updateWrapper);
        postCategoryService.deletePostCategory(id);
        postCategoryService.addPostCategory(id, categoryIds);
        return update > 0;
    }
}
