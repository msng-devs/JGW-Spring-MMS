package com.jaramgroupware.mms.domain.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


/**
 * 회원에 대한 정보를 담고 있는 클래스
 *
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 * @since 2023-03-07
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "MEMBER")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Member {

    /**
     * 해당 회원의 UID(Firebase uid) (PK)
     */
    @Id
    @Column(name = "MEMBER_PK", nullable = false, length = 28)
    private String id;

    /**
     * 회원의 실명
     */
    @Column(name = "MEMBER_NM", nullable = false, length = 45)
    private String name;

    /**
     * 회원의 이메일
     */
    @Email
    @Column(name = "MEMBER_EMAIL", nullable = false, unique = true, length = 255)
    private String email;

    /**
     * 회원의 권한 레벨
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ROLE_PK", nullable = false)
    private Role role;

    /**
     * 회원의 계정 활성 상태
     */
    @ColumnDefault("1")
    @Column(name = "MEMBER_STATUS", nullable = false)
    private boolean status;

    /**
     * Member 객체의 업데이트를 위한 함수
     *
     * @param member 업데이트 하고자 하는 Member 객체
     */
    public void update(Member member) {
        name = member.getName();
        role = member.getRole();
    }
}
