package com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsenceResponseControllerDto {
    private String id;
    private boolean status;
    private LocalDate expectedDateReturnSchool;
}
