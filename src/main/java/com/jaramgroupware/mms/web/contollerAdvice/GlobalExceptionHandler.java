package com.jaramgroupware.mms.web.contollerAdvice;

import com.jaramgroupware.mms.dto.general.ExceptionResponseDto;
import com.jaramgroupware.mms.utlis.exception.service.ServiceException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<ExceptionResponseDto> handleServiceException(ServiceException exception,WebRequest request){
        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                exception.getErrorCode().getErrorCode()
        );
        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(
                        ExceptionResponseDto
                                .builder()
                                .code(exception.getErrorCode().getErrorCode())
                                .message(exception.getMessage())
                                .status(exception.getErrorCode().getHttpStatus())
                                .timestamp(LocalDateTime.now())
                                .path(request.getContextPath())
                                .build()
                );

    }

    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    protected ResponseEntity<ExceptionResponseDto> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception, WebRequest request) {
        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "HttpMediaTypeNotSupportedException");
        return ResponseEntity
                .status(404)
                .body(
                        ExceptionResponseDto
                                .builder()
                                .code("MM-NULL-000")
                                .code("Not Defined Error")
                                .message("해당 요청 타입을 지원하지 않습니다.")
                                .status(HttpStatus.NOT_FOUND)
                                .timestamp(LocalDateTime.now())
                                .path(request.getContextPath())
                                .build()
                );
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    protected ResponseEntity<ExceptionResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, WebRequest request) {

        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "NOT_FOUND"
        );
        return ResponseEntity.status(404).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message("해당 요청 메소드를 지원하지 않습니다.")
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );
    }
    @ExceptionHandler({ IllegalArgumentException.class })
    protected ResponseEntity<ExceptionResponseDto> handleIllegalArgumentException(IllegalArgumentException exception, WebRequest request) {

        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "IllegalArgumentException"
        );
        return ResponseEntity.status(404).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message("잘못된 요청입니다.")
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );

    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    protected ResponseEntity<ExceptionResponseDto> handleIllegalArgumentException(DataIntegrityViolationException exception, WebRequest request) {

        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "DataIntegrityViolationException"
        );
        return ResponseEntity.status(500).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message("DB에 요청을 처리하는 중 오류가 발생했습니다.")
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );
    }

    @ExceptionHandler({ DuplicateKeyException.class })
    protected ResponseEntity<ExceptionResponseDto> handleDuplicateKeyException(DuplicateKeyException exception, WebRequest request) {

        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "DuplicateKeyException"
        );

        return ResponseEntity.status(500).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message("DB에 요청을 처리하는 중 알 수 없는 에러가 발생했습니다.")
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<ExceptionResponseDto> handleServerException(Exception exception, WebRequest request) {

        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                exception.getClass().getSimpleName()
        );

        return ResponseEntity.status(500).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message("알 수 없는 에러가 발생했습니다.")
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponseDto> processMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, WebRequest request) {
        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "MethodArgumentTypeMismatchException"
        );

        return ResponseEntity.status(400).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message("잘못된 입력값이 존재합니다.")
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> processValidationError(MethodArgumentNotValidException exception, WebRequest request) {
        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "MethodArgumentNotValidException"
        );
        BindingResult bindingResult = exception.getBindingResult();
        StringBuilder builder = new StringBuilder();
        builder.append("잘못된 입력값이 존재합니다! ");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("필드명 : (");
            builder.append(fieldError.getField());
            builder.append(") 오류 메시지: (");
            builder.append(fieldError.getDefaultMessage());
            builder.append(") 입력된 값: ");
            builder.append(fieldError.getRejectedValue());
            builder.append(" // ");
        }

        return ResponseEntity.status(400).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message(builder.toString())
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );

    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDto> processValidationError(ConstraintViolationException exception, WebRequest request) {
        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "ConstraintViolationException"
        );

        return ResponseEntity.status(400).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message("잘못된 입력값이 존재합니다.")
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDto> processHttpMessageNotReadableException(HttpMessageNotReadableException exception, WebRequest request) {
        log.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "HttpMessageNotReadableException"
        );

        return ResponseEntity.status(400).body(
                ExceptionResponseDto
                        .builder()
                        .code("MM-NULL-000")
                        .code("Not Defined Error")
                        .message("잘못된 입력값이 존재합니다.")
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .path(request.getContextPath())
                        .build()
        );
    }

}