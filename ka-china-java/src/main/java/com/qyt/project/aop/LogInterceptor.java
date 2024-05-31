package com.qyt.project.aop;

import com.qyt.project.annotation.LogAnnotation;
import com.qyt.project.constant.UserConstant;
import com.qyt.project.model.entity.OperationLog;
import com.qyt.project.model.vo.user.UserVo;
import com.qyt.project.service.OperationLogService;
import com.qyt.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * 请求响应日志 AOP
 *
 * @author Linskin
 **/
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    @Resource
    private UserService userService;

    @Resource
    private OperationLogService operationLogService;

    /**
     * 执行拦截
     */
    @Around("execution(* com.qyt.project.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Date currentDate = new Date();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成请求唯一 id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";
        // 输出请求日志
        log.info("request start，id: {}, path: {}, ip: {}, params: {}", requestId, url,
                httpServletRequest.getRemoteHost(), reqParam);
        // 执行原方法
        Object result = point.proceed();
        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {}ms", requestId, totalTimeMillis);

        //1.获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        //2.通过方法签名获取方法对象
        Method mMethod = signature.getMethod();
        LogAnnotation annotation = mMethod.getAnnotation(LogAnnotation.class);
        if (annotation == null) {
            return result;
        }
        String title = annotation.title();
        boolean saveLog = annotation.isSaveLog();
        if (saveLog) {
            Long userId = 0L;
            // 获取当前登录用户
            Object user = httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
            UserVo currentUserVo = (UserVo) user;
            if (currentUserVo != null && currentUserVo.getId() != null) {
                userId = currentUserVo.getId();
            }
            OperationLog operationLog = new OperationLog();
            operationLog.setRequestId(requestId);
            operationLog.setTitle(title);
            operationLog.setRequestMethod(method);
            operationLog.setOperationUrl(url);
            operationLog.setUserId(userId);
            operationLog.setOperationIp(httpServletRequest.getRemoteHost());
            operationLog.setOperationParam(reqParam);
            operationLog.setResult(result.toString());
            operationLog.setOperationTime(currentDate);
            operationLog.setCostTime(totalTimeMillis);
            operationLogService.save(operationLog);
        }
        return result;
    }
}

