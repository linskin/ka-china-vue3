package com.qyt.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.qyt.project.annotation.AuthCheck;
import com.qyt.project.annotation.LogAnnotation;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.common.IdRequest;
import com.qyt.project.common.IdsRequest;
import com.qyt.project.common.ResultUtils;
import com.qyt.project.constant.UserConstant;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.model.dto.log.OperationLogQueryDto;
import com.qyt.project.model.entity.OperationLog;
import com.qyt.project.model.vo.OperationLogVo;
import com.qyt.project.model.vo.user.UserSimpleVo;
import com.qyt.project.service.OperationLogService;
import com.qyt.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志接口控制器
 *
 * @author Linskin
 * @date 2023/6/13
 */
@RestController
@RequestMapping("/log")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    @Resource
    private UserService userService;

    /**
     * 删除操作日志
     *
     * @param idRequest 操作日志ID
     * @return 状态
     */
    @PostMapping("/delete")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "删除操作日志", isSaveLog = false)
    public ResultUtils<Boolean> deleteLog(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean delete = operationLogService.removeById(idRequest.getId());
        return ResultUtils.success(delete, "删除成功");
    }

    /**
     * 批量删除操作日志
     *
     * @param idsRequest 操作日志ID集合
     * @return 状态
     */
    @PostMapping("/delete/batch")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "批量删除操作日志")
    public ResultUtils<Boolean> deleteLogBatch(@RequestBody IdsRequest idsRequest) {
        if (idsRequest == null || idsRequest.getIds().size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean delete = operationLogService.removeBatchByIds(idsRequest.getIds());
        return ResultUtils.success(delete, "删除成功");
    }

    /**
     * 操作日志全部删除
     *
     * @return 状态
     */
    @DeleteMapping("/delete/all")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "操作日志全部删除")
    public ResultUtils<Boolean> deleteLogAll() {
        boolean delete = operationLogService.remove(new QueryWrapper<>());
        return ResultUtils.success(delete, "删除成功");
    }

    /**
     * 获取最新5条操作日志
     *
     * @return 操作日志列表
     */
    @GetMapping("/tail")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "获取最新5条操作日志", isSaveLog = false)
    public ResultUtils<List<OperationLogVo>> logTail() {
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("operationTime");
        queryWrapper.last("limit 5");
        List<OperationLog> logList = operationLogService.list(queryWrapper);
        List<OperationLogVo> operationLogVoList = logList.stream().map(this::translationToVo).collect(Collectors.toList());
        return ResultUtils.success(operationLogVoList);
    }

    /**
     * 操作日志分页
     *
     * @param operationLogQueryDto 操作日志查询信息
     * @return 操作日志分页数据
     */
    @GetMapping("/page")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "操作日志分页")
    public ResultUtils<Page<OperationLogVo>> logPage(OperationLogQueryDto operationLogQueryDto) {
        long current = 1;
        long size = 10;
        String title = null;
        String requestMethod = null;
        if (operationLogQueryDto != null) {
            current = operationLogQueryDto.getCurrent();
            size = operationLogQueryDto.getPageSize();
            title = operationLogQueryDto.getTitle();
            requestMethod = operationLogQueryDto.getRequestMethod();

        }
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(requestMethod), "requestMethod", requestMethod);
        Page<OperationLog> operationLogPage = operationLogService.page(new Page<>(current, size), queryWrapper);
        Page<OperationLogVo> operationLogVOPage = new PageDTO<>(operationLogPage.getCurrent(), operationLogPage.getSize(), operationLogPage.getTotal());
        List<OperationLogVo> operationLogVOList = operationLogPage.getRecords().stream().map(this::translationToVo).collect(Collectors.toList());
        operationLogVOPage.setRecords(operationLogVOList);
        return ResultUtils.success(operationLogVOPage);
    }

    /**
     * 操作日志转化
     *
     * @param operationLog 操作日志实体
     * @return 操作日志返回类
     */
    public OperationLogVo translationToVo(OperationLog operationLog) {
        OperationLogVo operationLogVo = new OperationLogVo();
        UserSimpleVo userSimpleVo = new UserSimpleVo();
        if (operationLog.getUserId() != null) {
            userSimpleVo = userService.getUserSimpleById(operationLog.getUserId());
        }
        operationLogVo.setId(operationLog.getId());
        operationLogVo.setRequestId(operationLog.getRequestId());
        operationLogVo.setTitle(operationLog.getTitle());
        operationLogVo.setRequestMethod(operationLog.getRequestMethod());
        operationLogVo.setOperationUrl(operationLog.getOperationUrl());
        operationLogVo.setUserInfo(userSimpleVo);
        operationLogVo.setOperationIp(operationLog.getOperationIp());
        operationLogVo.setOperationParam(operationLog.getOperationParam());
        operationLogVo.setResult(operationLog.getResult());
        operationLogVo.setOperationTime(operationLog.getOperationTime());
        operationLogVo.setCostTime(operationLog.getCostTime());
        return operationLogVo;
    }

}
