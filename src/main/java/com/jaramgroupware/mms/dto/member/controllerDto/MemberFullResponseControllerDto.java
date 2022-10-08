package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.BaseEntity;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberFullResponseControllerDto{

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

    public MemberResponseControllerDto toTiny(){
        return MemberResponseControllerDto
                .builder()
                .rankName(rankName)
                .rankID(rankID)
                .majorName(majorName)
                .leaveAbsence(leaveAbsence)
                .majorID(majorID)
                .year(year)
                .email(email)
                .entStudentID(studentID.substring(2,4))
                .build();
    }
}
