package com.jaramgroupware.mms.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberResponseDto {
    private String uid;
    private String email;
    private String name;
    private String cellPhoneNumber;
    private String studentId;
    private MajorResponseDto major;
    private RankResponseDto rank;
    private RoleResponseDto role;
    private Integer year;
    private LocalDate dateOfBirth;

    public MemberResponseDto(Member member, MemberInfo memberInfo){
        uid = member.getId();
        email = member.getEmail();
        name = member.getName();
        cellPhoneNumber = memberInfo.getPhoneNumber();
        studentId = memberInfo.getStudentID();
        major = new MajorResponseDto(memberInfo.getMajor());
        rank = new RankResponseDto(memberInfo.getRank());
        role = new RoleResponseDto(member.getRole());
        year = memberInfo.getYear();
        dateOfBirth = memberInfo.getDateOfBirth();
    }
}
