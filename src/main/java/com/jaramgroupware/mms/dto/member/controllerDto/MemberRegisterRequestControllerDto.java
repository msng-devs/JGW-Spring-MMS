package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberRegisterRequestServiceDto;
import lombok.*;

import javax.validation.constraints.*;

@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberRegisterRequestControllerDto {

    @NotEmpty(message = "인증코드가 비여있습니다!")
    private String authCode;

    @NotEmpty(message = "UID가 비여있습니다!")
    @Size(max = 28,min=28,message = "UID는 28자리여야 합니다.")
    private String id;

    @NotEmpty(message = "Email이 비여있습니다!")
    @Email(message = "email 형식이 잘못되었습니다!")
    private String email;

    @NotEmpty(message = "이름이 비여있습니다!")
    private String name;

    public MemberRegisterRequestServiceDto toServiceDto(Role role, MemberInfo memberInfo){
        return MemberRegisterRequestServiceDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .role(role)
                .status(true)
                .memberInfo(memberInfo)
                .build();
    }
}
