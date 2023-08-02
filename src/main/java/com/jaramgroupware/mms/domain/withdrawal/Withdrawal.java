package com.jaramgroupware.mms.domain.withdrawal;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "WITHDRAWAL")
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WITHDRAWAL_PK")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_MEMBER_PK")
    private Member member;

    @Column(name = "WITHDRAWAL_DEL_DATE",nullable = false)
    private LocalDate withdrawalDate;

    @Column(name = "WITHDRAWAL_CREATE_DATE",nullable = false)
    private LocalDate createDate;


}
