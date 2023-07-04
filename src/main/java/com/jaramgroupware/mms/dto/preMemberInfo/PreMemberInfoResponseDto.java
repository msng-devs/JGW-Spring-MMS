package com.jaramgroupware.mms.dto.preMemberInfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.registerCode.RegisterCodeResponseDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PreMemberInfoResponseDto {
    private Long id;
    private String studentId;
    private String name;
    private MajorResponseDto majorResponseDto;
    private Integer year;
    private RoleResponseDto roleResponseDto;
    private RankResponseDto rankResponseDto;

    private RegisterCodeResponseDto registerCode;

    public PreMemberInfoResponseDto(PreMemberInfo preMemberInfo){
        this.id = preMemberInfo.getId();
        this.studentId = preMemberInfo.getStudentId();
        this.name = preMemberInfo.getName();
        this.majorResponseDto = new MajorResponseDto(preMemberInfo.getMajor());
        this.year = preMemberInfo.getYear();
        this.roleResponseDto = new RoleResponseDto(preMemberInfo.getRole());
        this.rankResponseDto = new RankResponseDto(preMemberInfo.getRank());

        this.registerCode = (preMemberInfo.getRegisterCode() == null) ? null : new RegisterCodeResponseDto(preMemberInfo.getRegisterCode());
    }
}
