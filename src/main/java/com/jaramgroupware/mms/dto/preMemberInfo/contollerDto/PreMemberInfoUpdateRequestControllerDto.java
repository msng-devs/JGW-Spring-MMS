package com.jaramgroupware.mms.dto.preMemberInfo.contollerDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.dto.preMemberInfo.serviceDto.PreMemberInfoAddRequestServiceDto;
import com.jaramgroupware.mms.dto.preMemberInfo.serviceDto.PreMemberInfoUpdateRequestServiceDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PreMemberInfoUpdateRequestControllerDto {

    @NotEmpty(message = "학생번호가 비여있습니다!")
    @Size(max = 10, min = 10, message = "학생번호는 10자리여이야 합니다!")
    private String studentId;

    @NotEmpty(message = "이름이 비여있습니다!")
    @Size(max = 45, message = "이름은 최대 45자까지 가능합니다.")
    private String name;

    @NotNull(message = "role 정보가 없습니다!")
    private Long roleId;

    @NotNull(message = "회원 등급 정보가 없습니다!")
    private Long rankId;

    @NotNull(message = "전공 정보가 없습니다!")
    private Long majorId;

    @Positive(message = "기수는 양수여야 합니다!")
    private Integer year;

    public PreMemberInfoUpdateRequestServiceDto toServiceDto(Long id) {
        return PreMemberInfoUpdateRequestServiceDto.builder()
                .id(id)
                .studentId(studentId)
                .name(name)
                .roleId(roleId)
                .rankId(rankId)
                .majorId(majorId)
                .year(year)
                .build();
    }
}
