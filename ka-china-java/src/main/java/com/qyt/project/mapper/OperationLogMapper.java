package com.qyt.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qyt.project.model.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志数据库服务
 *
 * @author Linskin
 * @date 2023/6/13
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

}
