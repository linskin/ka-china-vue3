package com.qyt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qyt.project.common.IdRequest;
import com.qyt.project.model.dto.user.*;
import com.qyt.project.model.entity.User;
import com.qyt.project.model.vo.user.UserSimpleVo;
import com.qyt.project.model.vo.user.UserVo;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author Linskin
 * @date 2023/6/11
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param userLoginDto 用户登录实体
     * @param request      请求
     * @return 用户信息
     */
    UserVo userLogin(UserLoginDto userLoginDto, HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param userRegisterDto 用户注册实体
     * @return 用户ID
     */
    long userRegister(UserRegisterDto userRegisterDto);

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return 用户信息
     */
    UserVo getCurrentUser(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request 请求
     * @return 状态
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 创建用户
     *
     * @param userAddDto 用户添加实体
     * @return 用户ID
     */
    long addUser(UserAddDto userAddDto);

    /**
     * 用户删除
     *
     * @param idRequest ID
     * @param currentUser 当前用户信息
     * @return 状态
     */
    boolean deleteUser(IdRequest idRequest, UserVo currentUser);

    /**
     * 用户更新
     *
     * @param userUpdateDto 用户更新实体
     * @param currentUser 当前用户
     * @return 状态
     */
    boolean updateUser(UserUpdateDto userUpdateDto, UserVo currentUser);

    /**
     * 通过ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVo getUserById(Long userId);

    /**
     * 通过ID获取用户简单信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserSimpleVo getUserSimpleById(Long userId);

    /**
     * 修改角色
     *
     * @param userId 用户ID
     * @return 状态
     */
    boolean changeRole(Long userId, UserVo currentUser);

    /**
     * 修改密码
     *
     * @param userChangePasswordDto 用户修改密码信息
     * @param currentUser 当前登录用户
     * @return 状态
     */
    boolean changePassword(UserChangePasswordDto userChangePasswordDto, UserVo currentUser);

    /**
     * 重置密码
     *
     * @param userId 用户ID
     * @return 状态
     */
    boolean resetPassword(Long userId);

}
