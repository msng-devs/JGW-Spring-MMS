package com.jaramgroupware.mms.dto.memberInfo.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.rank.Rank;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoRegisterRequestServiceDto {

    private String phoneNumber;
    private String studentID;
    private Rank rank;
    private Major major;
    private LocalDate dateOfBirth;

    public MemberInfo toEntity(){
        return MemberInfo.builder()
                .phoneNumber(phoneNumber)
                .studentID(studentID)
                .year(LocalDate.now().getYear()-1984)
                .rank(rank)
                .major(major)
                .dateOfBirth(dateOfBirth)
                .build();
    }
}
