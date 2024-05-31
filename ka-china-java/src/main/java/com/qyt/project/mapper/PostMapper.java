package com.qyt.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qyt.project.model.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子数据库操作
 *
 * @author Linskin
 * @date 2023/6/15
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
