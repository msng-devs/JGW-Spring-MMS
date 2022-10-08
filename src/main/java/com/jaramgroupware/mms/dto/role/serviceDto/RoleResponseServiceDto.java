package com.jaramgroupware.mms.dto.role.serviceDto;

import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.role.controllerDto.RoleResponseControllerDto;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseServiceDto {

    private Integer id;
    private String name;

    public RoleResponseServiceDto(Role role){
        id = role.getId();
        name = role.getName();
    }

    public Role toEntity(){
        return Role.builder()
                .id(id)
                .name(name)
                .build();
    }

    public RoleResponseControllerDto toControllerDto(){
        return RoleResponseControllerDto.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Override
    public boolean equals(Object o){
        return this.toString().equals(o.toString());
    }
}
