package com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto;

import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceResponseControllerDto;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLeaveAbsenceResponseServiceDto {
    private String id;
    private boolean status;
    private LocalDate expectedDateReturnSchool;

    public MemberLeaveAbsenceResponseServiceDto(MemberLeaveAbsence memberLeaveAbsence) {
        id = memberLeaveAbsence.getId();
        status = memberLeaveAbsence.isStatus();
        expectedDateReturnSchool = memberLeaveAbsence.getExpectedDateReturnSchool();
    }

    public MemberLeaveAbsenceResponseControllerDto toControllerDto() {
        return MemberLeaveAbsenceResponseControllerDto.builder()
                .id(id)
                .status(status)
                .expectedDateReturnSchool(expectedDateReturnSchool)
                .build();
    }

    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }

}
