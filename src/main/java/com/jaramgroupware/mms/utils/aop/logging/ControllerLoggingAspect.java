package com.jaramgroupware.mms.utils.aop.logging;

import com.jaramgroupware.mms.utils.context.RequestIdContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.stream.Collectors;

@Order(-1)
@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class ControllerLoggingAspect {
    private final RequestIdContext requestIdContext;

    @Pointcut("within(org.springframework.web.bind.annotation.RestController)")
    public void loggingControllerMethods() {
    }

    @Before("loggingControllerMethods()")
    public void requestLogging() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if(attributes == null) return;

        var request = attributes.getRequest();

        var headers = request.getHeaderNames();
        var headerString = Collections.list(headers)
                .stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .collect(Collectors.joining(", "));
        var body = request.getParameterMap();
        var queryString = request.getQueryString();
        var method = request.getMethod();
        var requestURI = request.getRequestURI();
        var id = requestIdContext.getId();

        log.info("[Request] [ID : {}] > method={}, uri={}, headers=[{}], query={}, body={}", id, method, requestURI, headerString, queryString, body);

    }

    @AfterReturning(pointcut = "loggingControllerMethods()", returning = "response")
    public void responseLogging(Object response) {
        var servletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        var id = requestIdContext.getId();
        if (servletResponse != null) {
            log.info("[Response] [ID : {}] > status={}, response={}",id, servletResponse.getStatus()," [" + response + "]");
        }
    }
}
