package com.qyt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qyt.project.model.dto.category.CategoryAddDto;
import com.qyt.project.model.dto.category.CategoryUpdateDto;
import com.qyt.project.model.entity.Category;
import com.qyt.project.model.vo.category.CategorySimpleVo;
import com.qyt.project.model.vo.category.CategoryVo;

/**
 * 分类服务
 *
 * @author Linskin
 * @date 2023/6/14
 */
public interface CategoryService extends IService<Category> {

    /**
     * 创建分类
     *
     * @param categoryAddDto 分类创建信息
     * @param userId 当前用户ID
     * @return 分类ID
     */
    long addCategory(CategoryAddDto categoryAddDto, Long userId);

    /**
     * 更新分类
     *
     * @param categoryUpdateDto 分类更新信息
     * @return 状态
     */
    boolean updateCategory(CategoryUpdateDto categoryUpdateDto);

    /**
     * 获取分类简单返回信息
     *
     * @param categoryId 分类ID
     * @return CategorySimpleVo 分类简单返回信息
     */
    CategorySimpleVo getCategorySimpleById(Long categoryId);

    /**
     * 获取分类返回信息
     *
     * @param categoryId 分类ID
     * @return CategoryVo 分类返回信息
     */
    CategoryVo getCategoryById(Long categoryId);

}
