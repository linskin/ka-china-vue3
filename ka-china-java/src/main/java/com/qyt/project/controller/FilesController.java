package com.qyt.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.qyt.project.annotation.AuthCheck;
import com.qyt.project.annotation.LogAnnotation;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.common.ResultUtils;
import com.qyt.project.constant.UserConstant;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.model.dto.files.FilesQueryDto;
import com.qyt.project.model.entity.Files;
import com.qyt.project.model.vo.FilesVo;
import com.qyt.project.model.vo.user.UserSimpleVo;
import com.qyt.project.model.vo.user.UserVo;
import com.qyt.project.service.FilesService;
import com.qyt.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件接口控制器
 *
 * @author Linskin
 * @date 2023/6/14
 */
@RestController
@RequestMapping("/files")
public class FilesController {

    @Resource
    private FilesService filesService;

    @Resource
    private UserService userService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @param request 请求
     * @return 文件下载地址
     * @throws IOException IO异常
     */
    @PostMapping("/upload")
    @AuthCheck()
    @LogAnnotation(title = "文件上传")
    public ResultUtils<String> uploadFile(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String url = filesService.uploadFile(file, currentUser.getId());
        return ResultUtils.success(url);
    }

    /**
     * 文件下载
     *
     * @param uuid 文件UUID
     * @param response 响应
     * @throws IOException IO异常
     */
    @GetMapping("/download/{uuid}")
    @LogAnnotation(title = "文件下载", isSaveLog = false)
    public void downloadFile(@PathVariable String uuid, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(uuid)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        filesService.downloadFile(uuid, response);
    }

    /**
     * 文件分页
     *
     * @param filesQueryDto 文件分页信息
     * @return 文件分页数据
     */
    @GetMapping("/page")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "文件分页")
    public ResultUtils<Page<FilesVo>> filesPage(FilesQueryDto filesQueryDto) {
        long current = 1;
        long size = 10;
        String fileName = null;
        String fileType = null;
        String fileUuid = null;
        if (filesQueryDto != null) {
            current = filesQueryDto.getCurrent();
            size = filesQueryDto.getPageSize();
            fileName = filesQueryDto.getFileName();
            fileType = filesQueryDto.getFileType();
            fileUuid = filesQueryDto.getFileUuid();
        }

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(fileName), "fileName", fileName);
        queryWrapper.like(StringUtils.isNotBlank(fileType), "fileType", fileType);
        queryWrapper.like(StringUtils.isNotBlank(fileUuid), "fileUuid", fileUuid);
        queryWrapper.orderByDesc("updateTime");

        Page<Files> filesPage = filesService.page(new Page<>(current, size), queryWrapper);
        Page<FilesVo> filesVOPage = new PageDTO<>(filesPage.getCurrent(), filesPage.getSize(), filesPage.getTotal());
        List<FilesVo> filesVOList = filesPage.getRecords().stream().map(files -> {
            FilesVo filesVo = new FilesVo();
            BeanUtils.copyProperties(files, filesVo);
            UserSimpleVo userSimpleVo = new UserSimpleVo();
            if (files.getUploaderId() != null && files.getUploaderId() != 0) {
                userSimpleVo = userService.getUserSimpleById(files.getUploaderId());
            }
            filesVo.setUserInfo(userSimpleVo);
            return filesVo;
        }).collect(Collectors.toList());
        filesVOPage.setRecords(filesVOList);
        return ResultUtils.success(filesVOPage);
    }

}
