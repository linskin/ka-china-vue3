package com.qyt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyt.project.mapper.OperationLogMapper;
import com.qyt.project.model.entity.OperationLog;
import com.qyt.project.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务实现类
 *
 * @author Linskin
 * @date 2023/6/13
 */
@Service
@Slf4j
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

}
