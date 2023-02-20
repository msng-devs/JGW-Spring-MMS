package com.jaramgroupware.mms.dto.memberInfo.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.memberInfo.controllerDto.MemberInfoFullResponseControllerDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoResponseServiceDto {

    private String id;
    private String email;
    private String name;
    private String phoneNumber;
    private String studentID;
    private Integer majorID;
    private String majorName;
    private Integer rankID;
    private String rankName;
    private Integer roleID;
    private String roleName;
    private Integer year;
    private boolean leaveAbsence;
    private LocalDate dateOfBirth;
    private LocalDateTime createdDateTime;

    public MemberInfoResponseServiceDto(MemberInfo memberInfo) {
        id = memberInfo.getId();
        email = memberInfo.getMember().getEmail();
        name = memberInfo.getMember().getName();
        phoneNumber = memberInfo.getPhoneNumber();
        studentID = memberInfo.getStudentID();
        majorID = memberInfo.getMajor().getId();
        majorName = memberInfo.getMajor().getName();
        rankID = memberInfo.getRank().getId();
        rankName = memberInfo.getRank().getName();
        roleID = memberInfo.getMember().getRole().getId();
        roleName = memberInfo.getMember().getRole().getName();
        year = memberInfo.getYear();
        dateOfBirth = memberInfo.getDateOfBirth();
        createdDateTime = memberInfo.getCreatedDateTime();
    }

    public MemberInfo toEntity() {
        return MemberInfo.builder()
                .id(id)
                .member(
                        Member.builder()
                                .id(id)
                                .email(email)
                                .name(name)
                                .role(Role.builder()
                                        .id(roleID)
                                        .name(roleName)
                                        .build())
                                .build())

                .phoneNumber(phoneNumber)
                .studentID(studentID)
                .major(Major.builder()
                        .id(majorID)
                        .name(majorName)
                        .build()
                )
                .rank(Rank.builder()
                        .id(rankID)
                        .name(rankName)
                        .build()
                )
                .year(year)
                .dateOfBirth(dateOfBirth)
                .build();
    }

    public MemberInfoFullResponseControllerDto toControllerDto(){
        return MemberInfoFullResponseControllerDto
                .builder()
                .id(id)
                .email(email)
                .name(name)
                .phoneNumber(phoneNumber)
                .studentID(studentID)
                .majorID(majorID)
                .majorName(majorName)
                .rankID(rankID)
                .rankName(rankName)
                .roleID(roleID)
                .roleName(roleName)
                .year(year)
                .leaveAbsence(leaveAbsence)
                .dateOfBirth(dateOfBirth)
                .createdDateTime(createdDateTime)
                .build();
    }

    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
