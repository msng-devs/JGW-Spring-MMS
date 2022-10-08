package com.jaramgroupware.mms.dto.member.controllerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberAddRegisterCodeRequestControllerDto {

    @NotEmpty(message = "register code 가 비워져있습니다!")
    @Size(max = 28,min=28,message = "register code는 최소 6자리 최대 12자리 까지 가능합니다.")
    private String registerCode;

}
