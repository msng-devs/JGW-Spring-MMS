package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberResponseControllerDto {

    private String email;
    private String name;
    private String entStudentID;
    private Integer majorID;
    private String majorName;
    private Integer rankID;
    private String rankName;
    private Integer year;
    private boolean leaveAbsence;

}
