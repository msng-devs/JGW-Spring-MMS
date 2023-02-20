package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoUpdateRequestServiceDto;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberUpdateRequestControllerDto {

    @NotEmpty(message = "Email이 비여있습니다!")
    @Email(message = "email 형식이 잘못되었습니다!")
    private String email;

    @NotEmpty(message = "이름이 비여있습니다!")
    private String name;

    @NotEmpty(message = "휴대폰 번호가 비여있습니다!")
    @Pattern(regexp="(^$|[0-9]{11})")
    private String phoneNumber;

    @NotEmpty(message = "학생번호가 비여있습니다!")
    @Size(max = 10,min=10,message = "학생번호는 10자리여이야 합니다!")
    private String studentID;

    @NotNull(message = "전공 정보가 비여있습니다!")
    private Integer majorId;

    @NotNull(message = "회원 등급 정보가 없습니다!")
    private Integer rankId;

    @NotNull(message = "Role 등급이 비여있습니다!")
    private Integer roleId;

    @Positive(message = "기수는 양수여야 합니다!")
    private Integer year;

    @NotNull(message = "생년 월일이 비여있습니다!")
    private LocalDate dateOfBirth;

    @NotNull(message = "계정 활성 상태 여부가 비여있습니다!")
    private boolean status;

    public MemberUpdateRequestServiceDto toMemberServiceDto(Role role){
        return MemberUpdateRequestServiceDto.builder()
                .email(email)
                .name(name)
                .role(role)
                .status(status)
                .build();
    }

    public MemberInfoUpdateRequestServiceDto toMemberInfoServiceDto(Major major, Rank rank){
        return MemberInfoUpdateRequestServiceDto.builder()
                .phoneNumber(phoneNumber)
                .major(major)
                .rank(rank)
                .year(year)
                .studentID(studentID)
                .dateOfBirth(dateOfBirth)
                .build();
    }
}