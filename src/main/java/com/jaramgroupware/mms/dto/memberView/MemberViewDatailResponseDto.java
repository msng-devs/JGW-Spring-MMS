package com.jaramgroupware.mms.dto.memberView;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberView.MemberView;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberViewDatailResponseDto {

    private String uid;
    private String name;
    private String email;
    private RoleResponseDto role;
    private Boolean status;
    private String cellPhoneNumber;
    private String studentId;
    private Integer year;
    private RankResponseDto rank;
    private MajorResponseDto major;
    private LocalDate dateOfBirth;
    private Boolean isLeaveAbsence;

    public MemberViewDatailResponseDto(MemberView memberView){
        uid = memberView.getUid();
        name = memberView.getName();
        email = memberView.getEmail();
        status = memberView.getStatus();
        cellPhoneNumber = memberView.getCellPhoneNumber();
        studentId = memberView.getStudentId();
        year = memberView.getYear();
        dateOfBirth = memberView.getDateOfBirth();
        isLeaveAbsence = memberView.getIsLeaveAbsence();
        role = new RoleResponseDto(memberView.getRole(), memberView.getRoleName());
        rank = new RankResponseDto(memberView.getRank(), memberView.getRankName());
        major = new MajorResponseDto(memberView.getMajor(), memberView.getMajorName());

    }
}
