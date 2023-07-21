package com.jaramgroupware.mms.domain.memberView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.Immutable;

import java.time.LocalDate;

@Immutable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "MEMBER_DATA_VIEW")
public class MemberView {
    @Id
    @Column(name = "MEMBER_PK")
    private String uid;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ROLE")
    private Long role;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "STATUS")
    private Boolean status;

    @Column(name = "CELL_PHONE_NUMBER")
    private String cellPhoneNumber;

    @Column(name = "STUDENT_ID")
    private String studentId;

    @Column(name = "`YEAR`")
    private Integer year;

    @Column(name = "RANK")
    private Long rank;

    @Column(name = "RANK_NAME")
    private String rankName;

    @Column(name = "MAJOR")
    private Long major;

    @Column(name = "MAJOR_NAME")
    private String majorName;

    @Column(name = "DATEOFBIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "IS_LEAVE_ABSENCE")
    private Boolean isLeaveAbsence;
}
