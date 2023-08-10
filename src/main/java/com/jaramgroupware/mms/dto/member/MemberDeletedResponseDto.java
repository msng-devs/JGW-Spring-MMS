package com.jaramgroupware.mms.dto.member;

import com.jaramgroupware.mms.domain.member.Member;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDeletedResponseDto {

    private String uid;
    private String email;

    public MemberDeletedResponseDto(Member member){
        this.uid = uid;
        this.email = email;
    }

}
