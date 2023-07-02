package com.jaramgroupware.mms.utils.aop.routeOption.onlyToken;

import java.lang.annotation.*;

/**
 * Gateway Option 중 OnlyToken mode임을 명시하며, user의 uid 정보가 정상적으로 들어있는지 확인한다.
 */
@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnlyTokenOption {
}
