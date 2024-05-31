package com.qyt.project.model.dto.log;

import com.qyt.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 操作日志查询请求
 *
 * @author Linskin
 * @date 2023/6/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperationLogQueryDto extends PageRequest implements Serializable {

    /**
     * 模块标题
     */
    private String title;

    /**
     * 请求方式
     */
    private String requestMethod;

}
