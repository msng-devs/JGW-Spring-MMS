package com.jaramgroupware.mms.dto.memberInfo.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoRegisterRequestServiceDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberInfoRegisterRequestControllerDto {

    @Pattern(regexp="(^$|[0-9]{11})")
    private String phoneNumber;

    @NotEmpty(message = "학생번호가 비여있습니다!")
    @Size(max = 10,min=10,message = "학생번호는 10자리여이야 합니다!")
    private String studentID;

    @NotNull(message = "전공 정보가 비여있습니다!")
    private Integer majorId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    //기수 선정 공식 (현재 년도 - 1984)
    public MemberInfoRegisterRequestServiceDto toServiceDto(Rank rank) {
        return MemberInfoRegisterRequestServiceDto.builder()
                .phoneNumber(phoneNumber)
                .studentID(studentID)
                .major(Major.builder().id(majorId).build())
                .rank(rank)
                .dateOfBirth(dateOfBirth)
                .build();
    }
}
