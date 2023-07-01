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
    private Role role;
    private boolean status;

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .role(role)
                .status(status)
                .build();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
