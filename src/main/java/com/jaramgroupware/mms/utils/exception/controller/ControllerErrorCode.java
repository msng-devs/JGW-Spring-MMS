package com.jaramgroupware.mms.utils.exception.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ControllerErrorCode {

    GATEWAY_AUTH_MODE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Gateway Auth Error", "MM-CONTROLLER-001"),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "Not Authorized", "MM-CONTROLLER-002");

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}
