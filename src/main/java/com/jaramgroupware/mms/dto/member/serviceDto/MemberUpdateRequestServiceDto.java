package com.jaramgroupware.mms.dto.member.serviceDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequestServiceDto {

    private String targetId;
    private String email;
    private String name;
    private String phoneNumber;
    private String studentID;
    private Long majorId;
    private Long rankId;
    private Long roleId;
    private Integer year;
    private LocalDate dateOfBirth;
    private String modifiedBy;

    public Member toMemberEntity(Role role){
        return Member.builder()
                .email(email)
                .name(name)
                .role(role)
                .build();
    }

    public MemberInfo toMemberInfoEntity(Member member, Major major, Rank rank,LocalDateTime nowDateTime){
        var memberInfo = MemberInfo.builder()
                .member(member)
                .phoneNumber(phoneNumber)
                .studentID(studentID)
                .major(major)
                .rank(rank)
                .year(year)
                .dateOfBirth(dateOfBirth)
                .build();

        memberInfo.setModifiedBy(modifiedBy);
        memberInfo.setModifiedDateTime(nowDateTime);

        return memberInfo;
    }

}
