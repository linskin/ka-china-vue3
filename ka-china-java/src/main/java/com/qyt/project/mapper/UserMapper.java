package com.qyt.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qyt.project.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据库操作
 *
 * @author Linskin
 * @date 2023/6/10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
