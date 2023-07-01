package com.jaramgroupware.mms.utils.exception.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceException extends RuntimeException {
    private final ServiceErrorCode errorCode;
    private final String message;
}
