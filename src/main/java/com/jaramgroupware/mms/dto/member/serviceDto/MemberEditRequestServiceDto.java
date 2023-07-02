package com.jaramgroupware.mms.dto.member.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEditRequestServiceDto {

    private String targetId;
    private String modifiedBy;
    private String email;
    private String name;
    private String phoneNumber;
    private Integer majorId;

    public Member toMemberEntity(){
        return Member.builder()
                .email(email)
                .name(name)
                .build();
    }

    public MemberInfo toMemberInfoEntity(Member member, Major major){
        var memberInfo =  MemberInfo.builder()
                .member(member)
                .phoneNumber(phoneNumber)
                .major(major)
                .build();
        memberInfo.setModifiedBy(modifiedBy);
        memberInfo.setModifiedDateTime(LocalDateTime.now());
        return memberInfo;
    }
}