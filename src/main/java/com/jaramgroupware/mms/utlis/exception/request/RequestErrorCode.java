package com.jaramgroupware.mms.utlis.exception.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RequestErrorCode {
    QUERY_PARAM_NOT_VALID(HttpStatus.BAD_REQUEST, "Query Param is not Valid", "MM-REQUEST-001");

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}
