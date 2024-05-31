package com.qyt.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.mapper.CategoryMapper;
import com.qyt.project.model.dto.category.CategoryAddDto;
import com.qyt.project.model.dto.category.CategoryUpdateDto;
import com.qyt.project.model.entity.Category;
import com.qyt.project.model.vo.category.CategorySimpleVo;
import com.qyt.project.model.vo.category.CategoryVo;
import com.qyt.project.model.vo.user.UserSimpleVo;
import com.qyt.project.service.CategoryService;
import com.qyt.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 分类服务实现
 *
 * @author Linskin
 * @date 2023/6/14
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private UserService userService;

    public void validData(String name) {
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数不完整");
        }
        if (StringUtils.isNotBlank(name) && (name.length() < 2 || name.length() > 15)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "名称长度需在2-15之间");
        }
    }

    @Override
    public long addCategory(CategoryAddDto categoryAddDto, Long userId) {
        String name = categoryAddDto.getName();
        validData(name);
        Category category = new Category();
        category.setName(name);
        category.setUserId(userId);
        int insert = categoryMapper.insert(category);
        if (insert <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建失败，数据库错误");
        }
        return category.getId();
    }

    @Override
    public boolean updateCategory(CategoryUpdateDto categoryUpdateDto) {
        Long id = categoryUpdateDto.getId();
        String name = categoryUpdateDto.getName();
        if (id == null || id == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未收到分类Id");
        }
        validData(name);
        UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", name);
        int update = categoryMapper.update(new Category(), updateWrapper);
        return update > 0;
    }

    @Override
    public CategorySimpleVo getCategorySimpleById(Long categoryId) {
        CategorySimpleVo categorySimpleVo = new CategorySimpleVo();
        if (categoryId == 0) return categorySimpleVo;
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) return categorySimpleVo;
        BeanUtils.copyProperties(category, categorySimpleVo);
        return categorySimpleVo;
    }

    @Override
    public CategoryVo getCategoryById(Long categoryId) {
        CategoryVo categoryVo = new CategoryVo();
        if (categoryId == 0) return categoryVo;
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) return categoryVo;
        BeanUtils.copyProperties(category, categoryVo);
        UserSimpleVo userSimpleVo = new UserSimpleVo();
        if (category.getUserId() != null) {
            userSimpleVo = userService.getUserSimpleById(category.getUserId());
        }
        categoryVo.setUserInfo(userSimpleVo);
        return categoryVo;
    }
}
