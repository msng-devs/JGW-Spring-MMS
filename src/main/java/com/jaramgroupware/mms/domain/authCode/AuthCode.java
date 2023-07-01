package com.jaramgroupware.mms.domain.authCode;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import lombok.*;

import javax.persistence.*;

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
@Entity(name = "AUTH_CODE")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthCode {

    /**
     * 신규 회원가입을 위한 인증코드 (UUID, PK)
     */
    @Id
    @Column(name = "AUTH_CODE_PK", nullable = false, length = 36)
    private String id;

    /**
     * 인증코드에 해당되는 멤버 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_INFO_MEMBER_INFO_PK", nullable = false, unique = true)
    private MemberInfo memberInfo;
}
