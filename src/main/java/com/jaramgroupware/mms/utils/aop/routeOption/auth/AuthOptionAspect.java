package com.jaramgroupware.mms.utils.aop.routeOption.auth;

import com.jaramgroupware.mms.utils.exception.controller.ControllerErrorCode;
import com.jaramgroupware.mms.utils.exception.controller.ControllerException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AuthOption 어노테이션을 사용한 메소드에 대한 AOP 처리를 담당하는 클래스.
 * Gateway Option 중 AUTH mode를 처리하며, role 정보, user의 uid 정보가 정상적으로 들어있는지 확인한다.
 */
@Aspect
@Component
public class AuthOptionAspect {

    @Before("@annotation(authOption)")
    public void beforeRbacCheck(JoinPoint joinPoint, AuthOption authOption) {

        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var userUid = request.getHeader("user_pk");
        var roleId = request.getHeader("role_pk");

        if(userUid == null || roleId == null)
            throw new ControllerException(ControllerErrorCode.GATEWAY_AUTH_MODE_FAIL, "Gateway에서 오류가 발생했습니다. 인증이 제대로 진행되지 않았습니다.");

    }

    // 헤더 값 검증 이후 원래 메소드 실행
    @Around("@annotation(authOption)")
    public Object aroundRbacCheck(ProceedingJoinPoint joinPoint, AuthOption authOption) throws Throwable {

        return joinPoint.proceed();
    }
}
