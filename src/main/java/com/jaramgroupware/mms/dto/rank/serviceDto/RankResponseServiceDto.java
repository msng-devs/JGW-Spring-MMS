package com.jaramgroupware.mms.dto.rank.serviceDto;

import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.rank.controllerDto.RankResponseControllerDto;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankResponseServiceDto {

    private Integer id;
    private String name;

    public RankResponseServiceDto(Rank rank){
        id = rank.getId();
        name = rank.getName();
    }

    public Rank toEntity(){
        return Rank.builder()
                .id(id)
                .name(name)
                .build();
    }

    public RankResponseControllerDto toControllerDto(){
        return RankResponseControllerDto.builder()
                .id(id)
                .name(name)
                .build();
    }
    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
