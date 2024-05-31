package com.qyt.project.model.dto.files;

import com.qyt.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文件查询实体
 *
 * @author Linskin
 * @date 2023/6/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FilesQueryDto extends PageRequest implements Serializable {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 类型
     */
    private String fileType;

    /**
     * 文件UUID
     */
    private String fileUuid;

}