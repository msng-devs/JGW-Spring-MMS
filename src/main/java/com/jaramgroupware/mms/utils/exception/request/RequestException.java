package com.jaramgroupware.mms.utils.exception.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestException extends RuntimeException {
    private final RequestErrorCode errorCode;
    private final String message;
}
