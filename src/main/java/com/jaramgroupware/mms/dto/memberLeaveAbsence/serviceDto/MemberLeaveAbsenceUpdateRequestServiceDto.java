package com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto;

import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLeaveAbsenceUpdateRequestServiceDto {

    private String uid;
    private boolean isLeaveAbsence;
    private LocalDate expectedReturnDate;

    public MemberLeaveAbsence toEntity(){
        return MemberLeaveAbsence.builder()
                .status(isLeaveAbsence)
                .expectedDateReturnSchool(expectedReturnDate)
                .build();
    }
}
