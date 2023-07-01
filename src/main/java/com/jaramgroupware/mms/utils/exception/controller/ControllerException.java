package com.jaramgroupware.mms.utils.exception.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ControllerException extends RuntimeException{
    private final ControllerErrorCode errorCode;
    private final String message;
}
