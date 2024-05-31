package com.qyt.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.qyt.project.annotation.AuthCheck;
import com.qyt.project.annotation.LogAnnotation;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.common.IdRequest;
import com.qyt.project.common.ResultUtils;
import com.qyt.project.constant.UserConstant;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.model.dto.user.*;
import com.qyt.project.model.entity.User;
import com.qyt.project.model.vo.user.UserVo;
import com.qyt.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口控制器
 *
 * @author Linskin
 * @date 2023/6/12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userLoginDto 用户登录信息
     * @param request      请求
     * @return 用户信息
     */
    @PostMapping("/login")
    @LogAnnotation(title = "用户登录", isSaveLog = false)
    public ResultUtils<UserVo> userLogin(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        if (userLoginDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo userVo = userService.userLogin(userLoginDto, request);
        return ResultUtils.success(userVo, "登录成功");
    }

    /**
     * 用户注册
     *
     * @param userRegisterDto 用户注册信息
     * @return 用户ID
     */
    @PostMapping("/register")
    @LogAnnotation(title = "用户注册", isSaveLog = false)
    public ResultUtils<Long> userRegister(@RequestBody UserRegisterDto userRegisterDto) {
        if (userRegisterDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = userService.userRegister(userRegisterDto);
        return ResultUtils.success(userId, "注册成功");
    }

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return 用户信息
     */
    @GetMapping("/current")
    @AuthCheck()
    @LogAnnotation(title = "获取当前登录用户")
    public ResultUtils<UserVo> getCurrentUser(HttpServletRequest request) {
        UserVo currentUser = userService.getCurrentUser(request);
        return ResultUtils.success(currentUser);
    }

    /**
     * 用户注销
     *
     * @param request 请求
     * @return 状态
     */
    @PostMapping("/logout")
    @AuthCheck()
    @LogAnnotation(title = "用户注销")
    public ResultUtils<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 创建用户
     *
     * @param userAddDto 用户添加信息
     * @return 用户ID
     */
    @PostMapping("/add")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "用户添加信息")
    public ResultUtils<Long> addUser(@RequestBody UserAddDto userAddDto) {
        if (userAddDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = userService.addUser(userAddDto);
        return ResultUtils.success(userId, "创建成功");
    }

    /**
     * 用户删除
     *
     * @param idRequest 删除用户ID
     * @param request 请求
     * @return 状态
     */
    @DeleteMapping("/delete")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "用户删除")
    public ResultUtils<Boolean> deleteUser(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        boolean delete = userService.deleteUser(idRequest, currentUser);
        return ResultUtils.success(delete, "删除成功");
    }

    /**
     * 用户更新
     *
     * @param userUpdateDto 用户更新信息
     * @param request 请求
     * @return 状态
     */
    @PutMapping("/update")
    @AuthCheck()
    @LogAnnotation(title = "用户更新")
    public ResultUtils<Boolean> updateUser(@RequestBody UserUpdateDto userUpdateDto, HttpServletRequest request) {
        if (userUpdateDto == null || userUpdateDto.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        boolean result = userService.updateUser(userUpdateDto, currentUser);
        return ResultUtils.success(result, "更新成功");
    }

    /**
     * 修改角色
     *
     * @param idRequest 用户ID
     * @param request 请求
     * @return 状态
     */
    @PostMapping("/role/change")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "修改角色")
    public ResultUtils<Boolean> changeRole(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        boolean result = userService.changeRole(idRequest.getId(), currentUser);
        return ResultUtils.success(result, "修改成功");
    }

    /**
     * 修改密码
     *
     * @param userChangePasswordDto 用户修改密码信息
     * @param request 请求
     * @return 状态
     */
    @PostMapping("/password/change")
    @AuthCheck()
    @LogAnnotation(title = "修改密码", isSaveLog = false)
    public ResultUtils<Boolean> changePassword(@RequestBody UserChangePasswordDto userChangePasswordDto, HttpServletRequest request) {
        if (userChangePasswordDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        boolean result = userService.changePassword(userChangePasswordDto, currentUser);
        return ResultUtils.success(result, "修改成功");
    }

    /**
     * 重置密码
     *
     * @param idRequest 用户ID
     * @return 状态
     */
    @PostMapping("/password/reset")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "重置密码")
    public ResultUtils<Boolean> resetPassword(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.resetPassword(idRequest.getId());
        return ResultUtils.success(result, "修改成功");
    }

    /**
     * 用户分页
     *
     * @param userQueryDto 用户查询信息
     * @return 用户分页列表
     */
    @GetMapping("/page")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "用户分页")
    public ResultUtils<Page<UserVo>> userPage(UserQueryDto userQueryDto) {
        long current = 1;
        long pageSize = 10;
        String name = null;
        String account = null;
        if (userQueryDto != null) {
            current = userQueryDto.getCurrent();
            pageSize = userQueryDto.getPageSize();
            name = userQueryDto.getName();
            account = userQueryDto.getAccount();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(account), "account", account);
        queryWrapper.orderByDesc("updateTime");

        Page<User> userPage = userService.page(new Page<>(current, pageSize), queryWrapper);
        Page<UserVo> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVo> userVOList = userPage.getRecords().stream().map(user -> {
            UserVo userVO = new UserVo();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

}
