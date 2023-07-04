package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 회원 휴학 정보를 담고 있는 클래스
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
@Entity(name = "MEMBER_LEAVE_ABSENCE")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsence {

    /**
     * 회원 휴학 정보의 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_LEAVE_ABSENCE_PK")
    private Integer id;

    /**
     * 회원의 휴학여부
     */
    @Column(name = "MEMBER_LEAVE_ABSENCE_STATUS", nullable = false)
    private boolean status;

    /**
     * 회원의 휴학 예정일
     */
    @Column(name = "MEMBER_LEAVE_ABSENCE_EXPECTED_DATE_RETURN_SCHOOL")
    private LocalDate expectedDateReturnSchool;

    /**
     * 회원 휴학 정보의 대상 회원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_MEMBER_PK", nullable = false)
    private Member member;

    /**
     * MemberLeaveAbsence 객체의 업데이트를 위한 함수
     *
     * @param memberLeaveAbsence 업데이트 하고자 하는 MemberLeaveAbsence(Object)
     */
    public void update(MemberLeaveAbsence memberLeaveAbsence) {
        status = memberLeaveAbsence.isStatus();
        expectedDateReturnSchool = memberLeaveAbsence.getExpectedDateReturnSchool();
    }

}
