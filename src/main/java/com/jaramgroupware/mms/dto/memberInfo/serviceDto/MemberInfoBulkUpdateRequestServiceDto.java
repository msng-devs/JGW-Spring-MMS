package com.jaramgroupware.mms.dto.memberInfo.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.rank.Rank;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoBulkUpdateRequestServiceDto {

    private String id;
    private Member member;
    private String phoneNumber;
    private Major major;
    private Rank rank;
    private Integer year;
    private String studentID;
    private LocalDate dateOfBirth;

    public MemberInfo toEntity(){
        return MemberInfo.builder()
                .id(id)
                .member(member)
                .phoneNumber(phoneNumber)
                .studentID(studentID)
                .major(major)
                .rank(rank)
                .year(year)
                .dateOfBirth(dateOfBirth)
                .build();
    }

    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
