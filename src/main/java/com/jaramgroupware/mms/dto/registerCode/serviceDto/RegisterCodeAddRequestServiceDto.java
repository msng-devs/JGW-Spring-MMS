package com.jaramgroupware.mms.dto.registerCode.serviceDto;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCodeAddRequestServiceDto {


    private Long expireDay;
    private Long preMemberInfoId;
    private String createdBy;

    public RegisterCode toRegisterCodeEntity(String code,PreMemberInfo preMemberInfo,LocalDate nowDate){
        return RegisterCode.builder()
                .code(code)
                .preMemberInfo(preMemberInfo)
                .createBy(createdBy)
                .expiredAt(nowDate)
                .build();
    }

}
