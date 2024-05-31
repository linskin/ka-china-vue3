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
import com.qyt.project.model.dto.category.CategoryAddDto;
import com.qyt.project.model.dto.category.CategoryQueryDto;
import com.qyt.project.model.dto.category.CategoryUpdateDto;
import com.qyt.project.model.entity.Category;
import com.qyt.project.model.vo.category.CategorySimpleVo;
import com.qyt.project.model.vo.category.CategoryVo;
import com.qyt.project.model.vo.user.UserSimpleVo;
import com.qyt.project.model.vo.user.UserVo;
import com.qyt.project.service.CategoryService;
import com.qyt.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类请求控制器
 *
 * @author Linskin
 * @date 2023/6/14
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private UserService userService;

    /**
     * 创建分类
     *
     * @param categoryAddDto 分类创建信息
     * @param request        请求
     * @return 分类ID
     */
    @PostMapping("/add")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "创建分类")
    public ResultUtils<Long> addCategory(@RequestBody CategoryAddDto categoryAddDto, HttpServletRequest request) {
        if (categoryAddDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        long categoryId = categoryService.addCategory(categoryAddDto, currentUser.getId());
        return ResultUtils.success(categoryId, "创建成功");
    }

    /**
     * 删除分类
     *
     * @param idRequest 分类ID
     * @return 状态
     */
    @DeleteMapping("/delete")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "删除分类")
    public ResultUtils<Boolean> deleteCategory(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean delete = categoryService.removeById(idRequest.getId());
        return ResultUtils.success(delete, "删除成功");
    }

    /**
     * 批量删除分类
     *
     * @param idsRequest 分类ID集合
     * @return 状态
     */
    @PostMapping("/delete/batch")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "批量删除分类")
    public ResultUtils<Boolean> addCategoryBatch(@RequestBody IdsRequest idsRequest) {
        if (idsRequest == null || idsRequest.getIds().size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean delete = categoryService.removeBatchByIds(idsRequest.getIds());
        return ResultUtils.success(delete);
    }

    /**
     * 更新分类
     *
     * @param categoryUpdateDto 分类更新信息
     * @return 状态
     */
    @PutMapping("/update")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "更新分类")
    public ResultUtils<Boolean> updateCategory(@RequestBody CategoryUpdateDto categoryUpdateDto) {
        if (categoryUpdateDto == null || categoryUpdateDto.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean update = categoryService.updateCategory(categoryUpdateDto);
        return ResultUtils.success(update, "更新成功");
    }

    /**
     * 查询分类列表
     *
     * @return 分类集合
     */
    @GetMapping("/list")
    @LogAnnotation(title = "查询分类列表")
    public ResultUtils<List<CategorySimpleVo>> categoryList() {
        List<Category> categoryList = categoryService.list();
        List<CategorySimpleVo> categorySimpleVoList = categoryList.stream().map(category -> {
            CategorySimpleVo categorySimpleVo = new CategorySimpleVo();
            BeanUtils.copyProperties(category, categorySimpleVo);
            return categorySimpleVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(categorySimpleVoList);
    }

    /**
     * 分类集合
     *
     * @param categoryQueryDto 分类查询信息
     * @return 分类分页信息
     */
    @GetMapping("/page")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "分类分页")
    public ResultUtils<Page<CategoryVo>> categoryPage(CategoryQueryDto categoryQueryDto) {
        long current = 1;
        long pageSize = 10;
        String name = null;
        if (categoryQueryDto != null) {
            current = categoryQueryDto.getCurrent();
            pageSize = categoryQueryDto.getPageSize();
            name = categoryQueryDto.getName();
        }
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.orderByDesc("updateTime");
        Page<Category> categoryPage = categoryService.page(new Page<>(current, pageSize), queryWrapper);
        Page<CategoryVo> categoryVoPage = new PageDTO<>(categoryPage.getCurrent(), categoryPage.getSize(), categoryPage.getTotal());
        List<CategoryVo> categoryVoList = categoryPage.getRecords().stream().map(category -> {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            UserSimpleVo userSimpleVo = userService.getUserSimpleById(category.getUserId());
            categoryVo.setUserInfo(userSimpleVo);
            return categoryVo;
        }).collect(Collectors.toList());
        categoryVoPage.setRecords(categoryVoList);
        return ResultUtils.success(categoryVoPage);
    }
}
