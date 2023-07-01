package com.jaramgroupware.mms.utils.aop.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@Component
@Aspect
public class RequestLoggingAspect {
    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void logRequest(JoinPoint joinPoint) {

        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request;
        String requestURL;
        String method;
        String remoteAddr;
        String queryString;
        String userAgent;

        try {
            request = attributes.getRequest();
            requestURL = request.getRequestURL().toString();
            method = request.getMethod();
            remoteAddr = request.getRemoteAddr();
            queryString = request.getQueryString();
            userAgent = request.getHeader("User-Agent");
        } catch (NullPointerException e) {
            requestURL = "N/A";
            method = "N/A";
            remoteAddr = "N/A";
            queryString = "N/A";
            userAgent = "N/A";
        }


        log.info("Received request: URL={}, Method={}, RemoteAddr={}, QueryString={}, UserAgent={}",
                requestURL, method, remoteAddr, queryString, userAgent);
    }

    @AfterReturning(pointcut = "@annotation(org.springframework.web.bind.annotation.RequestMapping)", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request;
        String requestURL;
        String method;
        try {
            request = attributes.getRequest();
            requestURL = request.getRequestURL().toString();
            method = request.getMethod();
        } catch (NullPointerException e) {
            requestURL = "N/A";
            method = "N/A";
        }


        log.info("Response from {}: URL={}, Method={}, Result={}", joinPoint.getSignature().getName(),
                requestURL, method, result);
    }
}
