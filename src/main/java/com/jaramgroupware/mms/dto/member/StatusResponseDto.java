package com.jaramgroupware.mms.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StatusResponseDto {
    private String status;

    public StatusResponseDto(MemberStat memberStat){
        status = memberStat.getStatus();
    }
}
