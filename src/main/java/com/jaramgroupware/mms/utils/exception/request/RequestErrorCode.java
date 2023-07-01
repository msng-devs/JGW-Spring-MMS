package com.jaramgroupware.mms.utils.exception.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Request에 대한 validation 체크 및 query param 등 클라이언트가 제공한 정보가 유효하지 않을 경우 발생하는 Exception
 */
@Getter
@AllArgsConstructor
public enum RequestErrorCode {
    QUERY_PARAM_NOT_VALID(HttpStatus.BAD_REQUEST, "Query Param is not Valid", "MM-REQUEST-001");

    private final HttpStatus httpStatus;
    private final String title;
    private final String errorCode;
}
