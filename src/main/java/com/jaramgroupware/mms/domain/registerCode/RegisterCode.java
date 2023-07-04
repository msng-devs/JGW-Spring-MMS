package com.jaramgroupware.mms.domain.registerCode;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.role.Role;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 신규 회원가입을 위한 인증코드 정보를 담고 있는 클래스
 * @since 2023-06-25
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "REGISTER_CODE")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegisterCode {

    @Id
    @Column(name = "REGISTER_CODE_PK", nullable = false, length = 36)
    private String code;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRE_MEMBER_INFO_PRE_MEMBER_INFO_PK", nullable = false, unique = true)
    private PreMemberInfo preMemberInfo;

    @Column(name = "REGISTER_CODE_EXPIRE", nullable = false)
    private LocalDate expiredAt;

    @Column(name = "REGISTER_CODE_CREATE_BY", nullable = false)
    private String createBy;

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.expiredAt);
    }
}
