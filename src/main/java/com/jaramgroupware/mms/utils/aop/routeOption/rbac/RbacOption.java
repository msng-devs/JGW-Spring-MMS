package com.jaramgroupware.mms.utils.aop.routeOption.rbac;

import java.lang.annotation.*;
/**
 * Gateway Option 중 RBAC mode임을 명시하며, role 정보, user의 uid 정보가 정상적으로 들어있는지 확인한다. 또한 설정한 Role이 제대로 들어있는지 확인한다.
 */
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RbacOption {
    int role() default 1;
}
