package com.jaramgroupware.mms.dto.rank;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.rank.Rank;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RankResponseDto {

    private Long id;
    private String name;

    public RankResponseDto(Rank rank){
        id = rank.getId();
        name = rank.getName();
    }

}
