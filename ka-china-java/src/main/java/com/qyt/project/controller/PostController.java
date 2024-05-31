package com.qyt.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.qyt.project.annotation.AuthCheck;
import com.qyt.project.annotation.LogAnnotation;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.common.IdRequest;
import com.qyt.project.common.IdsRequest;
import com.qyt.project.common.ResultUtils;
import com.qyt.project.constant.UserConstant;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.model.dto.post.PostAddDto;
import com.qyt.project.model.dto.post.PostQueryDto;
import com.qyt.project.model.dto.post.PostUpdateDto;
import com.qyt.project.model.entity.Category;
import com.qyt.project.model.entity.Post;
import com.qyt.project.model.vo.category.CategorySimpleVo;
import com.qyt.project.model.vo.post.PostSimpleVo;
import com.qyt.project.model.vo.post.PostVo;
import com.qyt.project.model.vo.user.UserSimpleVo;
import com.qyt.project.model.vo.user.UserVo;
import com.qyt.project.service.CategoryService;
import com.qyt.project.service.PostCategoryService;
import com.qyt.project.service.PostService;
import com.qyt.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子接口控制器
 *
 * @author Linskin
 * @date 2023/6/15
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private PostCategoryService postCategoryService;

    @Resource
    private CategoryService categoryService;

    /**
     * 创建帖子
     *
     * @param postAddDto 帖子创建信息
     * @param request    请求
     * @return 帖子ID
     */
    @PostMapping("/add")
    @AuthCheck()
    @LogAnnotation(title = "创建帖子")
    public ResultUtils<Long> addPost(@RequestBody PostAddDto postAddDto, HttpServletRequest request) {
        if (postAddDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        long bannerId = postService.addPost(postAddDto, currentUser.getId());
        return ResultUtils.success(bannerId, "创建成功");
    }

    /**
     * 删除帖子
     *
     * @param idRequest 帖子ID
     * @param request   请求
     * @return 状态
     */
    @DeleteMapping("/delete")
    @AuthCheck()
    @LogAnnotation(title = "删除帖子")
    public ResultUtils<Boolean> deletePost(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        boolean delete = postService.deletePost(idRequest.getId(), currentUser);
        return ResultUtils.success(delete, "删除成功");
    }

    /**
     * 批量删除帖子
     *
     * @param idsRequest 帖子ID集合
     * @param request    请求
     * @return 状态
     */
    @PostMapping("/delete/batch")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "批量删除帖子")
    public ResultUtils<Boolean> deletePostBatch(@RequestBody IdsRequest idsRequest, HttpServletRequest request) {
        if (idsRequest == null || idsRequest.getIds().size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        boolean delete = postService.deletePostBatch(idsRequest.getIds(), currentUser);
        return ResultUtils.success(delete);
    }

    /**
     * 更新帖子
     *
     * @param postUpdateDto 帖子更新信息
     * @return 状态
     */
    @PutMapping("/update")
    @AuthCheck()
    @LogAnnotation(title = "更新帖子")
    public ResultUtils<Boolean> updatePost(@RequestBody PostUpdateDto postUpdateDto) {
        if (postUpdateDto == null || postUpdateDto.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean update = postService.updatePost(postUpdateDto);
        return ResultUtils.success(update, "更新成功");
    }

    /**
     * 前台获取帖子详情
     *
     * @param id 帖子ID
     * @return 帖子详情
     */
    @GetMapping("/detail/{id}")
    @LogAnnotation(title = "前台获取帖子详情")
    public ResultUtils<PostVo> postDetail(@PathVariable Long id) {
        if (id == null || id == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = postService.getById(id);
        PostVo postVo = new PostVo();
        if (post != null) {
            BeanUtils.copyProperties(post, postVo);
            UserSimpleVo userSimpleVo = userService.getUserSimpleById(post.getUserId());
            postVo.setUserInfo(userSimpleVo);
            List<Long> categoryIds = postCategoryService.getCategoryIds(post.getId());
            if (categoryIds.size() > 0) {
                List<Category> categories = categoryService.listByIds(categoryIds);
                List<CategorySimpleVo> categorySimpleVoList = categories.stream().map(category -> {
                    CategorySimpleVo categorySimpleVo = new CategorySimpleVo();
                    BeanUtils.copyProperties(category, categorySimpleVo);
                    return categorySimpleVo;
                }).collect(Collectors.toList());
                postVo.setCategories(categorySimpleVoList);
            }
        }
        return ResultUtils.success(postVo);
    }

    /**
     * 前台获取帖子分页
     *
     * @param postQueryDto 帖子分页信息
     * @return 帖子分页返回信息
     */
    @GetMapping("/page/front")
    @LogAnnotation(title = "前台获取帖子分页")
    public ResultUtils<Page<PostSimpleVo>> postFrontPage(PostQueryDto postQueryDto) {
        long current = 1;
        long pageSize = 10;
        String title = null;
        if (postQueryDto != null) {
            current = postQueryDto.getCurrent();
            pageSize = postQueryDto.getPageSize();
            title = postQueryDto.getTitle();
        }
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.orderByDesc("updateTime");
        Page<Post> postPage = postService.page(new Page<>(current, pageSize), queryWrapper);
        Page<PostSimpleVo> postSimpleVoPage = new PageDTO<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        List<PostSimpleVo> postSimpleVoList = postPage.getRecords().stream().map(post -> {
            PostSimpleVo postSimpleVo = new PostSimpleVo();
            BeanUtils.copyProperties(post, postSimpleVo);
            UserSimpleVo userSimpleVo = userService.getUserSimpleById(post.getUserId());
            postSimpleVo.setUserInfo(userSimpleVo);
            List<Long> categoryIds = postCategoryService.getCategoryIds(post.getId());
            if (categoryIds.size() > 0) {
                List<Category> categories = categoryService.listByIds(categoryIds);
                List<CategorySimpleVo> categorySimpleVoList = categories.stream().map(category -> {
                    CategorySimpleVo categorySimpleVo = new CategorySimpleVo();
                    BeanUtils.copyProperties(category, categorySimpleVo);
                    return categorySimpleVo;
                }).collect(Collectors.toList());
                postSimpleVo.setCategories(categorySimpleVoList);
            }
            return postSimpleVo;
        }).collect(Collectors.toList());
        postSimpleVoPage.setRecords(postSimpleVoList);
        return ResultUtils.success(postSimpleVoPage);
    }

    /**
     * 帖子分页
     *
     * @param postQueryDto 帖子分页信息
     * @return 帖子分页返回信息
     */
    @GetMapping("/page")
    @AuthCheck(anyRole = {UserConstant.ADMIN_ROLE, UserConstant.SUPER_ADMIN})
    @LogAnnotation(title = "帖子分页")
    public ResultUtils<Page<PostVo>> postPage(PostQueryDto postQueryDto) {
        long current = 1;
        long pageSize = 10;
        String title = null;
        if (postQueryDto != null) {
            current = postQueryDto.getCurrent();
            pageSize = postQueryDto.getPageSize();
            title = postQueryDto.getTitle();
        }
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.orderByDesc("updateTime");
        Page<Post> postPage = postService.page(new Page<>(current, pageSize), queryWrapper);
        Page<PostVo> postVoPage = new PageDTO<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        List<PostVo> postVoList = postPage.getRecords().stream().map(post -> {
            PostVo postVo = new PostVo();
            BeanUtils.copyProperties(post, postVo);
            UserSimpleVo userSimpleVo = userService.getUserSimpleById(post.getUserId());
            postVo.setUserInfo(userSimpleVo);
            List<Long> categoryIds = postCategoryService.getCategoryIds(post.getId());
            if (categoryIds.size() > 0) {
                List<Category> categories = categoryService.listByIds(categoryIds);
                List<CategorySimpleVo> categorySimpleVoList = categories.stream().map(category -> {
                    CategorySimpleVo categorySimpleVo = new CategorySimpleVo();
                    BeanUtils.copyProperties(category, categorySimpleVo);
                    return categorySimpleVo;
                }).collect(Collectors.toList());
                postVo.setCategories(categorySimpleVoList);
            }
            return postVo;
        }).collect(Collectors.toList());
        postVoPage.setRecords(postVoList);
        return ResultUtils.success(postVoPage);
    }

}
