package com.jaramgroupware.mms.dto.memberLeaveAbsence;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import lombok.*;

import java.time.LocalDate;
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsenceResponseDto {
    private Integer id;
    private String uid;
    private LocalDate expectedDateReturnSchool;
    private boolean status;

    public MemberLeaveAbsenceResponseDto(MemberLeaveAbsence memberLeaveAbsence){
        id = memberLeaveAbsence.getId();
        uid = memberLeaveAbsence.getMember().getId();
        expectedDateReturnSchool = memberLeaveAbsence.getExpectedDateReturnSchool();
        status = memberLeaveAbsence.isStatus();
    }
}
