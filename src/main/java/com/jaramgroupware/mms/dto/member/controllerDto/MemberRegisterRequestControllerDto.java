package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberRegisterRequestServiceDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberRegisterRequestControllerDto {

    @NotNull(message = "email이 비여있습니다!")
    @Email(message = "email 형식이 잘못되었습니다!")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{11})")
    private String phoneNumber;

    private LocalDate dateOfBirth;

    public MemberRegisterRequestServiceDto toServiceDto(String uid, String code) {
        return MemberRegisterRequestServiceDto.builder()
                .email(email)
                .phoneNumber((phoneNumber.isEmpty())?phoneNumber:"")
                .dateOfBirth((dateOfBirth==null)?null:dateOfBirth)
                .uid(uid)
                .code(code)
                .build();
    }
}
