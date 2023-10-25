package com.jaramgroupware.mms.utils.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestIdContext {
    private String id = null;
}
