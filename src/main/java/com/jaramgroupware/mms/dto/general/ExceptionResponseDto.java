package com.jaramgroupware.mms.dto.general;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponseDto {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
    private String code;
    private String message;
    private String path;
}
