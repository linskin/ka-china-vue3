package com.qyt.project.model.vo;

import com.qyt.project.model.vo.user.UserSimpleVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件返回实体
 *
 * @author Linskin
 * @date 2023/6/14
 */
@Data
public class FilesVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件UUID
     */
    private String fileUuid;

    /**
     * 文件md5
     */
    private String fileMd5;

    /**
     * 下载链接
     */
    private String downloadUrl;

    /**
     * 上传者
     */
    private UserSimpleVo userInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
