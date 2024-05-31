package com.qyt.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件实体
 *
 * @author Linskin
 * @date 2023/6/14
 */
@TableName(value = "files")
@Data
public class Files implements Serializable {
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
    private Long uploaderId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
