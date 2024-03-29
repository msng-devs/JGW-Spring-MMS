package com.jaramgroupware.mms.dto.member.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.utils.time.TimeUtility;
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
    private String name;
    private String phoneNumber;
    private Long majorId;

    public Member toMemberEntity(){
        return Member.builder()
                .name(name)
                .build();
    }

    public MemberInfo toMemberInfoEntity(Member member, Major major,LocalDateTime nowDateTime){
        var memberInfo =  MemberInfo.builder()
                .member(member)
                .phoneNumber(phoneNumber)
                .major(major)
                .build();
        memberInfo.setModifiedBy(modifiedBy);
        memberInfo.setModifiedDateTime(nowDateTime);
        return memberInfo;
    }
}