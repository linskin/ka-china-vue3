package com.qyt.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 *
 * @author Linskin
 * @date 2023/6/13
 */
@TableName(value = "operation_log")
@Data
public class OperationLog implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求URL
     */
    private String operationUrl;

    /**
     * 操作用户
     */
    private Long userId;

    /**
     * 主机地址
     */
    private String operationIp;

    /**
     * 请求参数
     */
    private String operationParam;

    /**
     * 返回参数
     */
    private String result;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 消耗时间
     */
    private Long costTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
