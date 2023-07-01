package com.jaramgroupware.mms.utlis.exception.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ServiceErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "Item Not Found", "MM-SERVICE-001");

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}