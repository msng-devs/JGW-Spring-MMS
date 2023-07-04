package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberEditRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberUpdateRequestServiceDto;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberEditRequestControllerDto {

    @NotEmpty(message = "Email이 비여있습니다!")
    @Email(message = "email 형식이 잘못되었습니다!")
    private String email;

    @NotEmpty(message = "이름이 비여있습니다!")
    private String name;

    @Pattern(regexp = "(^$|[0-9]{11})")
    private String phoneNumber;

    @NotNull(message = "전공 정보가 비여있습니다!")
    private Long majorId;

    public MemberEditRequestServiceDto toServiceDto(String modifiedBy,String targetMember) {
        return MemberEditRequestServiceDto.builder()
                .targetId(targetMember)
                .modifiedBy(modifiedBy)
                .email(email)
                .name(name)
                .phoneNumber(phoneNumber)
                .majorId(majorId)
                .build();
    }

}