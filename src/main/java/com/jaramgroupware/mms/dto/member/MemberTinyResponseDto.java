package com.jaramgroupware.mms.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.member.Member;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberTinyResponseDto {
    private String uid;
    private String email;
    private String name;

    public MemberTinyResponseDto(Member member) {
        this.uid = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
    }
}
