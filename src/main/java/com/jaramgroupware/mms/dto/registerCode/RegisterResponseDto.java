package com.jaramgroupware.mms.dto.registerCode;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegisterResponseDto {
    private String code;
    private LocalDate expiredDateTime;
    private Integer memberInfoId;

    private String studentId;
    private String name;
    private RoleResponseDto role;
    private RankResponseDto rank;
    private MajorResponseDto major;
    private Integer year;
    public RegisterResponseDto(RegisterCodeResponseDto dto, MemberInfo memberInfo){
        this.code = dto.getCode();
        this.expiredDateTime = dto.getExpiredDateTime();
        this.memberInfoId = memberInfo.getId();

        this.studentId = memberInfo.getStudentID();
        this.name = dto.getName();
        this.role = dto.getRole();
        this.rank = new RankResponseDto(memberInfo.getRank());
        this.major = new MajorResponseDto(memberInfo.getMajor());
        this.year = memberInfo.getYear();


    }
}
