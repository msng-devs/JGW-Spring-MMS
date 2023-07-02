package com.jaramgroupware.mms.utils.exception.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Service레이어 에서 발생하는 Exception
 */
@Getter
@AllArgsConstructor
public enum ServiceErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "Item Not Found", "MM-SERVICE-001"),
    NOT_FOUND_CODE(HttpStatus.FORBIDDEN, "Code Not Found", "MM-SERVICE-002"),
    CODE_EXPIRED(HttpStatus.FORBIDDEN, "Register Code is Expired", "MM-SERVICE-003");

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}