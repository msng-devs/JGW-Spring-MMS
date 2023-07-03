package com.jaramgroupware.mms.domain.preMemberInfo;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "ROLE_ROLE_PK", nullable = false)
    private Role role;

    public void update(PreMemberInfo newPreMemberInfo){
        this.studentId = newPreMemberInfo.getStudentId();
        this.year = newPreMemberInfo.getYear();
        this.rank = newPreMemberInfo.getRank();
        this.major = newPreMemberInfo.getMajor();
        this.name = newPreMemberInfo.getName();
        this.role = newPreMemberInfo.getRole();
    }
}
