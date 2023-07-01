package com.jaramgroupware.mms.utlis.exception.request;

import com.jaramgroupware.mms.utlis.exception.service.ServiceErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestException  extends RuntimeException{
    private final RequestErrorCode errorCode;
    private final String message;
}
