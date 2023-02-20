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
public class MemberInfoAddRequestServiceDto {
    private String id;
    private String phoneNumber;
    private String studentId;
    private Integer year;
    private Rank rank;
    private Major major;
    private LocalDate dateOfBirth;


    public MemberInfo toEntity(){
        return MemberInfo.builder()
                .id(id)
                .member(Member.builder().id(id).build())
                .phoneNumber(phoneNumber)
                .studentID(studentId)
                .year((year != null)? year : LocalDate.now().getYear()-1984)
                .rank(rank)
                .major(major)
                .dateOfBirth(dateOfBirth)
                .build();
    }

    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}