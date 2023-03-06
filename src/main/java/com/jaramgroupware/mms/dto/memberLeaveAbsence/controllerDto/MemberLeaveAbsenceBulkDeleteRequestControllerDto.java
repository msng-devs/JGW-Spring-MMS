package com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsenceBulkDeleteRequestControllerDto {

    @NotNull(message = "MemberLeaveAbsence_ids가 비워져있습니다!")
    private Set<Integer> MemberLeaveAbsenceIDs;
}
