package com.qyt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qyt.project.model.dto.post.PostAddDto;
import com.qyt.project.model.dto.post.PostUpdateDto;
import com.qyt.project.model.entity.Post;
import com.qyt.project.model.vo.user.UserVo;

import java.util.List;

/**
 * 帖子服务
 *
 * @author Linskin
 * @date 2023/6/15
 */
public interface PostService extends IService<Post> {

    /**
     * 创建帖子
     *
     * @param postAddDto 帖子添加实体
     * @param userId 当前用户ID
     * @return 帖子ID
     */
    long addPost(PostAddDto postAddDto, Long userId);

    /**
     * 删除帖子
     *
     * @param postId 帖子ID
     * @param currentUser 当前登录用户
     * @return 状态
     */
    boolean deletePost(Long postId, UserVo currentUser);

    /**
     * 批量删除帖子
     *
     * @param postIds 帖子ID集合
     * @param currentUser 当前登录用户
     * @return 状态
     */
    boolean deletePostBatch(List<Long> postIds, UserVo currentUser);

    /**
     * 更新帖子
     *
     * @param postUpdateDto 帖子更新实体
     * @return 状态
     */
    boolean updatePost(PostUpdateDto postUpdateDto);


}
