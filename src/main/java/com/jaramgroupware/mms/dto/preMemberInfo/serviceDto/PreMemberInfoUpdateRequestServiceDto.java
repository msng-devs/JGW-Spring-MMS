package com.jaramgroupware.mms.dto.preMemberInfo.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreMemberInfoUpdateRequestServiceDto {

    private Long id;
    private String studentId;
    private String name;
    private Long roleId;
    private Long rankId;
    private Long majorId;
    private Integer year;
    private Long expireDay;
    private LocalDate expectedDateReturnSchool;
    public PreMemberInfo toEntity(Role role, Rank rank, Major major){
        return PreMemberInfo.builder()
                .studentId(studentId)
                .name(name)
                .role(role)
                .rank(rank)
                .major(major)
                .year(year)
                .expectedDateReturnSchool(expectedDateReturnSchool)
                .build();
    }
}
