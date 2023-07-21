package com.jaramgroupware.mms.dto.memberView;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberView.MemberView;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberViewResponseDto {

    private String uid;
    private String name;
    private String email;
    private String studentId;
    private Integer year;
    private RankResponseDto rank;
    private MajorResponseDto major;

    public MemberViewResponseDto(MemberView memberView){
        uid = memberView.getUid();
        name = memberView.getName();
        email = memberView.getEmail();
        studentId = memberView.getStudentId().substring(1, 3);
        year = memberView.getYear();
        rank = new RankResponseDto(memberView.getRank(), memberView.getRankName());
        major = new MajorResponseDto(memberView.getMajor(), memberView.getMajorName());

    }
}
