package com.jaramgroupware.mms.utils.exception.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ControllerErrorCode {

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}
