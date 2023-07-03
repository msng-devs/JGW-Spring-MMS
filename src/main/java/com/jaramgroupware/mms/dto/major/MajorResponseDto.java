package com.jaramgroupware.mms.dto.major;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.major.Major;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MajorResponseDto {
    private Long id;
    private String name;

    public MajorResponseDto(Major major){
        id = major.getId();
        name = major.getName();
    }

}
