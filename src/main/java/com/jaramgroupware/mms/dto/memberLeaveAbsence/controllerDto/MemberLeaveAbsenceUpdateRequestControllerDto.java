package com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceUpdateRequestServiceDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsenceUpdateRequestControllerDto {

    @NotNull(message = "휴학 여부가 비여있습니다!")
    private boolean status;

    private LocalDate expectedDateReturnSchool;

    public MemberLeaveAbsenceUpdateRequestServiceDto toServiceDto() {
        return MemberLeaveAbsenceUpdateRequestServiceDto.builder()
                .status(status)
                .expectedDateReturnSchool(expectedDateReturnSchool)
                .build();
    }
}