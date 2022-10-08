package com.jaramgroupware.mms.dto.general.controllerDto;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionMessageDto {
    private HttpStatus status;
    private Integer type;
    private String title;
    private String detail;
}
