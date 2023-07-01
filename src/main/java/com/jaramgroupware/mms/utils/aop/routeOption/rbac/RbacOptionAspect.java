package com.jaramgroupware.mms.utils.aop.routeOption.rbac;

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
 * RbacOption 어노테이션을 사용한 메소드에 대한 AOP 처리를 담당하는 클래스.
 * Gateway Option 중 RBAC mode를 처리하며, role 정보, user의 uid 정보가 정상적으로 들어있는지 확인한다. 또한 설정한 Role이 제대로 들어있는지 확인한다.
 */
@Aspect
@Component
public class RbacOptionAspect {

    @Before("@annotation(rbacOption)")
    public void beforeRbacCheck(JoinPoint joinPoint, RbacOption rbacOption) {
        // 헤더 값 검증 로직 구현
        // 예를 들어, Authorization 헤더 값 검증
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var userUid = request.getHeader("user_pk");
        var roleId = request.getHeader("role_pk");

        if(!isNotNullAndCorrectRole(userUid, roleId, rbacOption.role()))
            throw new ControllerException(ControllerErrorCode.GATEWAY_AUTH_MODE_FAIL, "Gateway에서 오류가 발생했습니다. 인증이 제대로 진행되지 않았습니다.");
    }

    @Around("@annotation(rbacOption)")
    public Object aroundRbacCheck(ProceedingJoinPoint joinPoint, RbacOption rbacOption) throws Throwable {
        // 헤더 값 검증 이후 원래 메소드 실행
        return joinPoint.proceed();
    }

    private boolean isNotNullAndCorrectRole(String userUid, String roleId, Integer optionRole) {
        return userUid != null && roleId != null && optionRole <= Integer.parseInt(roleId);
    }
}
