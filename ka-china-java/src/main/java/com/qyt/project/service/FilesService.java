package com.qyt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qyt.project.model.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件服务
 *
 * @author Linskin
 * @date 2023/6/14
 */
public interface FilesService extends IService<Files> {

    /**
     * 文件上传
     * @param file 文件
     * @param id 用户ID
     * @return 文件下载链接
     * @throws IOException IO异常
     */
    String uploadFile(MultipartFile file, Long id) throws IOException;

    /**
     * 文件下载
     * @param uuid 文件UUID
     * @param response 响应
     * @throws IOException IO异常
     */
    void downloadFile(String uuid, HttpServletResponse response) throws IOException;
}
