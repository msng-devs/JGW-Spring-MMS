package com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceUpdateRequestServiceDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsenceUpdateRequestControllerDto {

    @NotNull(message = "휴가/휴학 여부가 비여있습니다!")
    private boolean isLeaveAbsence;

    private LocalDate expectedReturnDate;

    public MemberLeaveAbsenceUpdateRequestServiceDto toServiceDto(String uid) {
        return MemberLeaveAbsenceUpdateRequestServiceDto.builder()
                .uid(uid)
                .isLeaveAbsence(isLeaveAbsence)
                .expectedReturnDate(expectedReturnDate)
                .build();
    }
}
