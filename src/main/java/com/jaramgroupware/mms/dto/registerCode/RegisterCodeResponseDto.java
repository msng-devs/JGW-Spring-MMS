package com.jaramgroupware.mms.dto.registerCode;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
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
public class RegisterCodeResponseDto {
    private String code;
    private LocalDate expiredDateTime;
    private Long preMemberInfoId;


    public RegisterCodeResponseDto(RegisterCode registerCode){
        this.code = registerCode.getCode();
        this.expiredDateTime = registerCode.getExpiredAt();
        this.preMemberInfoId = registerCode.getPreMemberInfo().getId();
    }
}
