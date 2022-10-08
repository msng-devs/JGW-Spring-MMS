package com.jaramgroupware.mms.dto.major.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.dto.major.controllerDto.MajorResponseControllerDto;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorResponseServiceDto {

    private Integer id;
    private String name;

    public MajorResponseServiceDto(Major major){
        id = major.getId();
        name = major.getName();
    }

    public Major toEntity(){
        return Major.builder()
                .id(id)
                .name(name)
                .build();
    }

    public MajorResponseControllerDto toControllerDto(){
        return MajorResponseControllerDto.builder()
                .id(id)
                .name(name)
                .build();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
