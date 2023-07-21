package com.jaramgroupware.mms.domain.preMemberInfo;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.domain.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PRE_MEMBER_INFO")
public class PreMemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRE_MEMBER_INFO_PK")
    private Long id;

    @Column(name = "PRE_MEMBER_INFO_STUDENT_ID", nullable = false)
    private String studentId;

    @Column(name = "PRE_MEMBER_INFO_YEAR", nullable = false)
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "RANK_RANK_PK", nullable = false)
    private Rank rank;

    @ManyToOne
    @JoinColumn(name = "MAJOR_MAJOR_PK", nullable = false)
    private Major major;

    @Column(name = "PRE_MEMBER_INFO_NM", nullable = false)
    private String name;

    //null이면 휴학상태가 아닌 상태.
    @Column(name = "PRE_MEMBER_INFO_EXPECTED_DATE_RETURN_SCHOOL", nullable = false)
    private LocalDate expectedDateReturnSchool;

    @ManyToOne
    @JoinColumn(name = "ROLE_ROLE_PK", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "preMemberInfo",fetch = FetchType.LAZY)
    private RegisterCode registerCode;

    public void update(PreMemberInfo newPreMemberInfo){
        this.studentId = newPreMemberInfo.getStudentId();
        this.year = newPreMemberInfo.getYear();
        this.rank = newPreMemberInfo.getRank();
        this.major = newPreMemberInfo.getMajor();
        this.name = newPreMemberInfo.getName();
        this.role = newPreMemberInfo.getRole();
    }
}
