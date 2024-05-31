package com.qyt.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.common.IdRequest;
import com.qyt.project.constant.UserConstant;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.mapper.UserMapper;
import com.qyt.project.model.dto.user.*;
import com.qyt.project.model.entity.User;
import com.qyt.project.model.vo.user.UserSimpleVo;
import com.qyt.project.model.vo.user.UserVo;
import com.qyt.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务实现类
 *
 * @author Linskin
 * @date 2023/6/11
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "qyt";

    @Override
    public UserVo userLogin(UserLoginDto userLoginDto, HttpServletRequest request) {
        String account = userLoginDto.getAccount();
        String password = userLoginDto.getPassword();
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数不完整");
        }
        if (account.length() <= 4 || account.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "账号错误");
        }
        if (password.length() < 8 || password.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "密码错误");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        queryWrapper.eq("password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("用户名与密码不匹配");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, userVo);
        return userVo;
    }

    @Override
    public long userRegister(UserRegisterDto userRegisterDto) {
        String name = userRegisterDto.getName();
        String account = userRegisterDto.getAccount();
        String password = userRegisterDto.getPassword();
        String rePassword = userRegisterDto.getRePassword();
        if (StringUtils.isAnyBlank(name, account, password, rePassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数不完整");
        }
        if (StringUtils.isNotBlank(name) && (name.length() < 2 || name.length() > 10)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "姓名长度需在2-10之间");
        }
        if (StringUtils.isNotBlank(account) && (account.length() <= 4 || account.length() > 16)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "账号长度需在4-16之间");
        }
        if (StringUtils.isNotBlank(password) && (password.length() <= 8 || password.length() > 16)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "密码长度需在8-16之间");
        }
        if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(rePassword) && !password.equals(rePassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "两次输入的密码不一致");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        User user = new User();
        user.setName(name);
        user.setAccount(account);
        user.setPassword(encryptPassword);
        user.setRole(UserConstant.DEFAULT_ROLE);
        int insert = userMapper.insert(user);
        if (insert <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    @Override
    public UserVo getCurrentUser(HttpServletRequest request) {
        Object user = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        UserVo currentUserVo = (UserVo) user;
        if (currentUserVo == null || currentUserVo.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        long userId = currentUserVo.getId();
        User currentUser = userMapper.selectById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        BeanUtils.copyProperties(currentUser, currentUserVo);
        return currentUserVo;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public long addUser(UserAddDto userAddDto) {
        String name = userAddDto.getName();
        String account = userAddDto.getAccount();
        String role = userAddDto.getRole();

        if (StringUtils.isAnyBlank(name, account, role)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数不完整");
        }
        if (StringUtils.isNotBlank(name) && (name.length() < 2 || name.length() > 10)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "姓名长度需在2-10之间");
        }
        if (StringUtils.isNotBlank(account) && (account.length() <= 4 || account.length() > 16)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "账号长度需在4-16之间");
        }
        if (!(UserConstant.DEFAULT_ROLE.equals(role) || UserConstant.ADMIN_ROLE.equals(role))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "角色选择错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + UserConstant.DEFAULT_USER_PASSWORD).getBytes());
        User user = new User();
        user.setName(name);
        user.setAccount(account);
        user.setPassword(encryptPassword);
        user.setRole(role);
        int insert = userMapper.insert(user);
        if (insert <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "新建失败，数据库错误");
        }
        return user.getId();
    }

    @Override
    public boolean deleteUser(IdRequest idRequest, UserVo currentUser) {
        Long userId = idRequest.getId();
        if (userId == null || userId == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未收到用户ID");
        }
        if (UserConstant.DEFAULT_ROLE.equals(currentUser.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败，用户不存在");
        }
        if (UserConstant.SUPER_ADMIN.equals(user.getRole())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "无法删除初始管理员");
        }
        if (UserConstant.ADMIN_ROLE.equals(user.getRole()) && !UserConstant.SUPER_ADMIN.equals(currentUser.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        int delete = userMapper.deleteById(userId);
        if (delete <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败，数据库错误");
        }
        return false;
    }

    @Override
    public boolean updateUser(UserUpdateDto userUpdateDto, UserVo currentUser) {
        Long id = userUpdateDto.getId();
        String name = userUpdateDto.getName();
        String avatar = userUpdateDto.getAvatar();
        if (id == null || id == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未收到用户ID");
        }
        if (StringUtils.isAnyBlank(name)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请输入姓名");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败，用户不存在");
        }
        if (UserConstant.DEFAULT_ROLE.equals(user.getRole()) && !(currentUser.getId().equals(user.getId()) || UserConstant.ADMIN_ROLE.equals(currentUser.getRole()))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (UserConstant.ADMIN_ROLE.equals(user.getRole()) && !(currentUser.getId().equals(user.getId()) || UserConstant.SUPER_ADMIN.equals(currentUser.getRole()))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (UserConstant.SUPER_ADMIN.equals(user.getRole()) && !currentUser.getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", name);
        updateWrapper.set("avatar", avatar);
        int update = userMapper.update(new User(), updateWrapper);
        return update > 0;
    }

    @Override
    public UserVo getUserById(Long userId) {
        UserVo userVO = new UserVo();
        if (userId == 0) return userVO;
        User user = userMapper.selectById(userId);
        if (user == null) return userVO;
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserSimpleVo getUserSimpleById(Long userId) {
        UserSimpleVo userSimpleVo = new UserSimpleVo();
        if (userId == 0) return userSimpleVo;
        User user = userMapper.selectById(userId);
        if (user == null) return userSimpleVo;
        BeanUtils.copyProperties(user, userSimpleVo);
        return userSimpleVo;
    }

    @Override
    public boolean changeRole(Long userId, UserVo currentUser) {
        if (userId == null || userId == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未收到用户ID");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "修改失败，用户不存在");
        }

        if (UserConstant.ADMIN_ROLE.equals(user.getRole()) && !UserConstant.SUPER_ADMIN.equals(currentUser.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        if (UserConstant.SUPER_ADMIN.equals(user.getRole())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作失败，无法修改初始管理员角色");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.getId());
        updateWrapper.set("role", UserConstant.ADMIN_ROLE.equals(user.getRole()) ? UserConstant.DEFAULT_ROLE : UserConstant.ADMIN_ROLE);
        int update = userMapper.update(new User(), updateWrapper);
        return update > 0;
    }

    @Override
    public boolean changePassword(UserChangePasswordDto userChangePasswordDto, UserVo currentUser) {
        String account = userChangePasswordDto.getAccount();
        String oldPassword = userChangePasswordDto.getOldPassword();
        String newPassword = userChangePasswordDto.getNewPassword();
        String reNewPassword = userChangePasswordDto.getReNewPassword();
        if (StringUtils.isAnyBlank(account, oldPassword, newPassword, reNewPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数不完整");
        }
        if (!account.equals(currentUser.getAccount())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "操作失败，仅支持修改自己的密码");
        }
        if (StringUtils.isNotBlank(oldPassword) && (oldPassword.length() <= 8 || oldPassword.length() > 16)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "密码长度需在8-16之间");
        }
        if (StringUtils.isNotBlank(newPassword) && (newPassword.length() <= 8 || newPassword.length() > 16)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "密码长度需在8-16之间");
        }
        if (StringUtils.isNotBlank(newPassword) && StringUtils.isNotBlank(reNewPassword) && !newPassword.equals(reNewPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "两次输入的密码不一致");
        }
        String encryptOldPassword = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        queryWrapper.eq("password", encryptOldPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        String encryptNewPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.getId());
        updateWrapper.set("password", encryptNewPassword);
        int update = userMapper.update(new User(), updateWrapper);
        return update > 0;
    }

    @Override
    public boolean resetPassword(Long userId) {
        if (userId == null || userId == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未收到用户ID");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "重置失败，用户不存在");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + UserConstant.DEFAULT_USER_PASSWORD).getBytes());
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.getId());
        updateWrapper.set("password", encryptPassword);
        int update = userMapper.update(new User(), updateWrapper);
        return update > 0;
    }
}
