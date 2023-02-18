package com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsenceUpdateRequestServiceDto {

    private boolean status;
    private LocalDate expectedDateReturnSchool;

    public MemberLeaveAbsence toEntity(){
        return MemberLeaveAbsence.builder()
                .status(status)
                .expectedDateReturnSchool(expectedDateReturnSchool)
                .build();
    }

    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }

}

