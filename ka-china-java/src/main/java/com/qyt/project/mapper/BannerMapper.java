package com.qyt.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qyt.project.model.entity.Banner;
import org.apache.ibatis.annotations.Mapper;

/**
 * banner数据库操作
 *
 * @author Linskin
 * @date 2023/6/13
 */
@Mapper
public interface BannerMapper extends BaseMapper<Banner> {
}
