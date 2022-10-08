package com.jaramgroupware.mms.dto.member.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberFullResponseControllerDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseServiceDto {

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
    private LocalDate dateofbirth;
    private LocalDateTime createdDateTime;

    public MemberResponseServiceDto(Member member){
        id = member.getId();
        email = member.getEmail();
        name = member.getName();
        phoneNumber = member.getPhoneNumber();
        studentID = member.getStudentID();
        majorID = member.getMajor().getId();
        majorName = member.getMajor().getName();
        roleID = member.getRole().getId();
        roleName = member.getRole().getName();
        rankID = member.getRank().getId();
        rankName = member.getRank().getName();
        year = member.getYear();
        leaveAbsence = member.isLeaveAbsence();
        dateofbirth = member.getDateOfBirth();
        createdDateTime = member.getCreatedDateTime();
    }
    public Member toEntity(){
        return Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .phoneNumber(phoneNumber)
                .studentID(studentID)
                .major(
                        Major.builder()
                                .id(majorID)
                                .name(majorName)
                                .build()
                )
                .rank(
                        Rank.builder()
                                .id(rankID)
                                .name(rankName)
                                .build()
                )
                .role(
                        Role.builder()
                                .id(roleID)
                                .name(roleName)
                                .build()
                )
                .year(year)
                .leaveAbsence(leaveAbsence)
                .dateOfBirth(dateofbirth)
                .build();
    }
    public MemberFullResponseControllerDto toControllerDto(){
        return MemberFullResponseControllerDto
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
                .dateofbirth(dateofbirth)
                .createdDateTime(createdDateTime)
                .build();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
