package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "MEMBER_LEAVE_ABSENCE")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberLeaveAbsence {

    @Id
    @Column(name = "MEMBER_LEAVE_ABSENCE_PK", nullable = false, length = 28)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "MEMBER_LEAVE_ABSENCE_PK",nullable = false)
    private Member member;

    @Column(name="MEMBER_LEAVE_ABSENCE_STATUS",nullable = false)
    private boolean status;

    @Column(name="MEMBER_LEAVE_ABSENCE_EXPECTED_DATE_RETURN_SCHOOL")
    private LocalDate expectedDateReturnSchool;


    public void update(MemberLeaveAbsence memberLeaveAbsence){
        status = memberLeaveAbsence.isStatus();
        expectedDateReturnSchool = memberLeaveAbsence.getExpectedDateReturnSchool();
    }

}
