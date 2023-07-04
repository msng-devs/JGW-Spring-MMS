package com.jaramgroupware.mms.dto.registerCode.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.registerCode.serviceDto.RegisterCodeAddRequestServiceDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegisterCodeAddRequestControllerDto {


    @Min(value = 1, message = "만료일은 1일 이상이여야 합니다!")
    @Max(value = 30, message = "만료일은 30일 이하여야 합니다!")
    @NotNull(message = "만료일 정보가 없습니다!")
    private Long expireDay;


    public RegisterCodeAddRequestServiceDto toServiceDto(String createdBy, Long preMemberInfoId) {
        return RegisterCodeAddRequestServiceDto.builder()
                .expireDay(expireDay)
                .createdBy(createdBy)
                .preMemberInfoId(preMemberInfoId)
                .build();
    }
}
