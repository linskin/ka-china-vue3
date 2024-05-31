package com.qyt.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.mapper.FilesMapper;
import com.qyt.project.model.entity.Files;
import com.qyt.project.service.FilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件服务实现
 *
 * @author Linskin
 * @date 2023/6/14
 */

@Slf4j
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements FilesService {

    @Resource
    private FilesMapper filesMapper;

    @Value("${files.upload.path}")
    private String uploadPath;

    @Value("${files.upload.address}")
    private String uploadAddress;

    private Files getFileByMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fileMd5", md5);
        List<Files> filesList = filesMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    @Override
    public String uploadFile(MultipartFile file, Long id) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFiles = new File(uploadPath + fileUUID);
        File parentFiles = uploadFiles.getParentFile();
        if (!parentFiles.exists()) {
            parentFiles.mkdirs();
        }
        String url;
        String md5 = SecureUtil.md5(file.getInputStream());
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getDownloadUrl();
        } else {
            file.transferTo(uploadFiles);
            url = uploadAddress + "/files/download/" + fileUUID;

        }
        Files saveFiles = new Files();
        saveFiles.setFileName(originalFilename);
        saveFiles.setFileType(type);
        saveFiles.setFileSize(String.valueOf(size / 1024));
        saveFiles.setFileMd5(md5);
        saveFiles.setFileUuid(fileUUID);
        saveFiles.setDownloadUrl(url);
        saveFiles.setUploaderId(id);
        int insert = filesMapper.insert(saveFiles);
        if (insert <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }
        return url;
    }

    @Override
    public void downloadFile(String uuid, HttpServletResponse response) throws IOException {
        File uploadFiles = new File(uploadPath + uuid);
        String suffix = uuid.split("\\.")[uuid.split("\\.").length - 1];
        ServletOutputStream os = response.getOutputStream();
        List<String> attachmentFileExtNames = CollUtil.newArrayList("jpg", "jpeg", "png", "JPG", "JPEG", "webp", "jfif");
        if (attachmentFileExtNames.contains(suffix)) {
            response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(uuid, "UTF-8"));
        } else if ("mp4".equals(suffix)) {
            response.setContentType("video/mp4");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(uuid, "UTF-8"));
            response.setContentLength(FileUtil.readBytes(uploadFiles).length);
            response.setHeader("Content-Range", String.valueOf(FileUtil.readBytes(uploadFiles).length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
        }else {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(uuid, "UTF-8"));
        }
        os.write(FileUtil.readBytes(uploadFiles));
        os.flush();
        os.close();
    }
}
