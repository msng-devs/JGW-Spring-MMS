package com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceAddRequestServiceDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsenceAddRequestControllerDto {
    @NotEmpty(message = "UID가 비여있습니다!")
    @Size(max = 28,min=28,message = "UID는 28자리여야 합니다.")
    private String id;

    @NotNull(message = "휴학 여부가 비여있습니다!")
    private boolean status;

    private LocalDate expectedDateReturnSchool;

    public MemberLeaveAbsenceAddRequestServiceDto toServiceDto(){
        return MemberLeaveAbsenceAddRequestServiceDto.builder()
                .id(id)
                .member(Member.builder().id(id).build())
                .status(status)
                .expectedDateReturnSchool(expectedDateReturnSchool)
                .build();
    }
}
