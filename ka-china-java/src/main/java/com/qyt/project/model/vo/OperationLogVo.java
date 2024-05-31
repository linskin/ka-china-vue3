package com.qyt.project.model.vo;

import com.qyt.project.model.vo.user.UserSimpleVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志返回
 *
 * @author Linskin
 * @date 2023/6/13
 */
@Data
public class OperationLogVo implements Serializable {

    /**
     * id
     */
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
     * 创建用户
     */
    private UserSimpleVo userInfo;

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

    private static final long serialVersionUID = 1L;

}
