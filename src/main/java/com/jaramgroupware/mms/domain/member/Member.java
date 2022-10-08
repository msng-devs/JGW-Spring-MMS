package com.jaramgroupware.mms.domain.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.BaseEntity;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverrides({
        @AttributeOverride(name = "createdDateTime",column = @Column(name = "MEMBER_CREATED_DTTM")),
        @AttributeOverride(name = "modifiedDateTime",column = @Column(name = "MEMBER_MODIFIED_DTTM")),
        @AttributeOverride(name = "createBy",column = @Column(name = "MEMBER_CREATED_BY",length = 30)),
        @AttributeOverride(name = "modifiedBy",column = @Column(name = "MEMBER_MODIFIED_BY",length = 30)),
})
@Entity(name = "MEMBER")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Member extends BaseEntity {

    @Id
    @Column(name = "MEMBER_PK",length = 28)
    private String id;

    @Email
    @Column(name = "MEMBER_EMAIL",nullable = false,length =256)
    private String email;

    @Column(name = "MEMBER_NM",nullable = false,length =45)
    private String name;

    @Column(name="MEMBER_CELL_PHONE_NUMBER",length =15)
    private String phoneNumber;

    @Column(name= "MEMBER_STUDENT_ID",nullable = false,unique = true,length =45)
    private String studentID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAJOR_MAJOR_PK",nullable = false)
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RANK_RANK_PK",nullable = false)
    private Rank rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ROLE_PK",nullable = false)
    private Role role;

    @Column(name="MEMBER_YEAR",nullable = false)
    private Integer year;

    @Column(name="MEMBER_LEAVE_ABSENCE",nullable = false)
    private boolean leaveAbsence;

    @Column(name="MEMBER_DATEOFBIRTH",nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    public void update(Member member,String who){
        name = member.getName();
        phoneNumber = member.getPhoneNumber();
        major = member.getMajor();
        rank = member.getRank();
        role = member.getRole();
        year = member.year;
        modifiedDateTime = LocalDateTime.now();
        leaveAbsence = member.isLeaveAbsence();
        modifiedBy = who;
    }


}
