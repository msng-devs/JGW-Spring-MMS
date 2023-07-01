package com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLeaveAbsenceAddRequestServiceDto {

    private Member member;
    private boolean status;
    private LocalDate expectedDateReturnSchool;

    public MemberLeaveAbsence toEntity(){
        return MemberLeaveAbsence.builder()
                .member(member)
                .status(status)
                .expectedDateReturnSchool(expectedDateReturnSchool)
                .build();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }

}


