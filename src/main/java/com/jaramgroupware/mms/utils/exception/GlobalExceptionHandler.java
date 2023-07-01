package com.jaramgroupware.mms.utils.exception;

import com.jaramgroupware.mms.dto.general.controllerDto.ExceptionMessageDto;
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


import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.jaramgroupware.mms.utils.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    protected ResponseEntity<ExceptionMessageDto> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception, WebRequest request) {

        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "HttpMediaTypeNotSupportedException"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.NOT_FOUND)
                .error("HttpMediaTypeNotSupportedException")
                .code(null)
                .message(exception.getMessage())
                .path(request.getContextPath())
                .build()
                , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    protected ResponseEntity<ExceptionMessageDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, WebRequest request) {

        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "NOT_FOUND"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.NOT_FOUND)
                .error("NOT_FOUND")
                .code(null)
                .message("해당 Path를 찾을 수 없습니다.")
                .path(request.getContextPath())
                .build()
                , HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity<ExceptionMessageDto> handleCustomException(CustomException exception, WebRequest request) {

        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                exception.getErrorCode().getErrorCode().getClass().getSimpleName()
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(exception.getErrorCode().getHttpStatus())
                .error(exception.getErrorCode().getError())
                .code(exception.getErrorCode().getErrorCode())
                .message(exception.getErrorCode().getMessage() + " " +exception.getMessage())
                .path(request.getContextPath())
                .build()
                , exception.getErrorCode().getHttpStatus());
    }
    @ExceptionHandler({ IllegalArgumentException.class })
    protected ResponseEntity<ExceptionMessageDto> handleIllegalArgumentException(IllegalArgumentException exception, WebRequest request) {

        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "IllegalArgumentException"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .error("BAD_REQUEST_PARAMS")
                .code(null)
                .message(exception.getMessage())
                .path(request.getContextPath())
                .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    protected ResponseEntity<ExceptionMessageDto> handleIllegalArgumentException(DataIntegrityViolationException exception, WebRequest request) {

        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "DataIntegrityViolationException"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .error("BAD_REQUEST_PARAMS")
                .code(null)
                .message("잘못된 외래키가 존재합니다. id를 다시 확인해주세요")
                .path(request.getContextPath())
                .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DuplicateKeyException.class })
    protected ResponseEntity<ExceptionMessageDto> handleDuplicateKeyException(DuplicateKeyException exception, WebRequest request) {

        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "DuplicateKeyException"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .error("DUPLICATE_KEY")
                .code(null)
                .message("중복되는 키가 존재합니다!"+exception.getRootCause().getMessage())
                .path(request.getContextPath())
                .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<ExceptionMessageDto> handleServerException(Exception exception, WebRequest request) {
        logger.info("error : {} {}",exception.getClass().getSimpleName(),exception.getMessage());
        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "INTERNAL_SERVER_ERROR"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(INTERNAL_SERVER_ERROR.getHttpStatus())
                .error(INTERNAL_SERVER_ERROR.getError())
                .code(INTERNAL_SERVER_ERROR.getErrorCode())
                .message(INTERNAL_SERVER_ERROR.getMessage())
                .path(request.getContextPath())
                .build()
                ,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionMessageDto> processMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, WebRequest request) {
        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "MethodArgumentTypeMismatchException"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .error("BAD_REQUEST_PARAMS")
                .code(null)
                .message(exception.getMessage())
                .path(request.getContextPath())
                .build()
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessageDto> processValidationError(MethodArgumentNotValidException exception, WebRequest request) {
        logger.info("UID = ({}) Request = ({}) Raise = ({})",
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

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .error("METHOD_ARGUMENT_NOT_VALID")
                .code(null)
                .message(builder.toString())
                .path(request.getContextPath())
                .build()
                ,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionMessageDto> processValidationError(ConstraintViolationException exception, WebRequest request) {
        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "ConstraintViolationException"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .error("REQUEST_ARGUMENT_NOT_VALID")
                .code(null)
                .message(exception.getMessage())
                .path(request.getContextPath())
                .build()
                ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionMessageDto> processHttpMessageNotReadableException(HttpMessageNotReadableException exception, WebRequest request) {
        logger.info("UID = ({}) Request = ({}) Raise = ({})",
                request.getHeader("user_uid"),
                request.getContextPath(),
                "HttpMessageNotReadableException"
        );

        return new ResponseEntity<>(ExceptionMessageDto.builder()
                .timestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST)
                .error("HttpMessageNotReadableException")
                .code(null)
                .message("입력 형식이 잘못됬습니다. 다시 확인하세요")
                .path(request.getContextPath())
                .build()
                ,HttpStatus.BAD_REQUEST);
    }

}