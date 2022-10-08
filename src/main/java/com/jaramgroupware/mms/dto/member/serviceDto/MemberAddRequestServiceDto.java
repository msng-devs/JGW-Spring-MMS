package com.jaramgroupware.mms.dto.member.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAddRequestServiceDto {

    private String id;
    private String email;
    private String name;
    private String phoneNumber;
    private String studentID;
    private Major major;
    private Rank rank;
    private Role role;
    private Integer year;
    private boolean leaveAbsence;
    private LocalDate dateOfBirth;

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .phoneNumber(phoneNumber)
                .studentID(studentID)
                .major(major)
                .rank(rank)
                .role(role)
                .year((year != null)? year : LocalDate.now().getYear()-1984)
                .leaveAbsence(leaveAbsence)
                .dateOfBirth(dateOfBirth)
                .build();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
