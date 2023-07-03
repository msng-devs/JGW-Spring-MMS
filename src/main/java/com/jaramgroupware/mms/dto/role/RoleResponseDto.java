package com.jaramgroupware.mms.dto.role;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.role.Role;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
public class RoleResponseDto {

    private Long id;
    private String name;

    public RoleResponseDto(Role role){
        id = role.getId();
        name = role.getName();
    }


}
