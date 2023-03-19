package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberFullResponseControllerDto {
    private String id;
    private String name;
    private String email;
    private Integer roleID;
    private String roleName;
    private boolean status;

    public MemberResponseControllerDto toTiny(){
        return MemberResponseControllerDto.builder()
                .name(name)
                .email(email)
                .status(status)
                .build();
    }

}
