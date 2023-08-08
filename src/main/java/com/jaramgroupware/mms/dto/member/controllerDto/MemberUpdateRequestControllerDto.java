package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class MemberUpdateRequestControllerDto {

    @NotEmpty(message = "Email이 비여있습니다!")
    @Email(message = "email 형식이 잘못되었습니다!")
    private String email;

    @NotEmpty(message = "이름이 비여있습니다!")
    @Size(max = 45, message = "이름은 최대 45자까지 가능합니다.")
    private String name;

    @Pattern(regexp = "(^$|[0-9]{3}-[0-9]{4}-[0-9]{4})")
    private String phoneNumber;

    @NotEmpty(message = "학생번호가 비여있습니다!")
    @Size(max = 10, min = 10, message = "학생번호는 10자리여이야 합니다!")
    private String studentId;

    @NotNull(message = "전공 정보가 비여있습니다!")
    private Long majorId;

    @NotNull(message = "회원 등급 정보가 없습니다!")
    private Long rankId;

    @NotNull(message = "Role 등급이 비여있습니다!")
    private Long roleId;

    @Positive(message = "기수는 양수여야 합니다!")
    private Integer year;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    public MemberUpdateRequestServiceDto toServiceDto(String modifiedBy,String targetMember) {
        return MemberUpdateRequestServiceDto.builder()
                .targetId(targetMember)
                .modifiedBy(modifiedBy)
                .email(email)
                .name(name)
                .phoneNumber(phoneNumber)
                .studentID(studentId)
                .majorId(majorId)
                .rankId(rankId)
                .roleId(roleId)
                .year(year)
                .dateOfBirth(dateOfBirth)
                .build();
    }
}