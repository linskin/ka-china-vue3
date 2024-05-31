package com.qyt.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qyt.project.model.entity.Files;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件数据库操作
 *
 * @author Linskin
 * @date 2023/6/14
 */
@Mapper
public interface FilesMapper extends BaseMapper<Files> {

}
