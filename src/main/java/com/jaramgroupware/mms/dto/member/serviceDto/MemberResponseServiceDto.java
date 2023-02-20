package com.jaramgroupware.mms.dto.member.serviceDto;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.role.Role;
import lombok.*;


@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseServiceDto {

    private String id;
    private String email;
    private String name;
    private Integer roleID;
    private String roleName;
    private boolean status;

    public MemberResponseServiceDto(Member member){
        id = member.getId();
        email = member.getEmail();
        name = member.getName();
        roleID = member.getRole().getId();
        roleName = member.getRole().getName();
        status = member.isStatus();
    }
    public Member toEntity(){
        return Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .role(
                        Role.builder()
                                .id(roleID)
                                .name(roleName)
                                .build()
                )
                .status(status)
                .build();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
