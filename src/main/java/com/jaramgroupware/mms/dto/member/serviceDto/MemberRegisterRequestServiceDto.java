package com.jaramgroupware.mms.dto.member.serviceDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterRequestServiceDto {


    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String uid;
    private String code;

    public Member toMemberEntity(PreMemberInfo preMemberInfo){
        return Member.builder()
                .id(uid)
                .name(preMemberInfo.getName())
                .role(preMemberInfo.getRole())
                .email(email)
                .status(true)
                .build();
    }

    public MemberInfo toMemberInfoEntity(PreMemberInfo preMemberInfo,Member member){
        var newMemberInfo = MemberInfo.builder()
                .major(preMemberInfo.getMajor())
                .studentID(preMemberInfo.getStudentId())
                .rank(preMemberInfo.getRank())
                .year(preMemberInfo.getYear())
                .phoneNumber(phoneNumber)
                .dateOfBirth(dateOfBirth)
                .build();

        newMemberInfo.setCreatedDateTime(LocalDateTime.now());
        newMemberInfo.setCreateBy("system");

        newMemberInfo.setModifiedDateTime(LocalDateTime.now());
        newMemberInfo.setModifiedBy("system");

        return newMemberInfo;
    }


}
