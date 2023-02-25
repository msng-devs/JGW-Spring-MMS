package com.jaramgroupware.mms.dto.memberInfo.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberInfoFullResponseControllerDto {

    private Integer id;
    private String memberID;
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
    private LocalDate dateOfBirth;
    private LocalDateTime createdDateTime;
    private boolean status;

    public MemberInfoResponseControllerDto toTiny() {
        return MemberInfoResponseControllerDto
                .builder()
                .memberID(memberID)
                .email(email)
                .name(name)
                .studentID(studentID.substring(2,4))
                .majorID(majorID)
                .majorName(majorName)
                .rankID(rankID)
                .rankName(rankName)
                .year(year)
                .status(status)
                .build();
    }
}
