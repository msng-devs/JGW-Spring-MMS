package com.jaramgroupware.mms.domain.memberInfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.BaseEntity;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원정보를 담고 있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverrides({
        @AttributeOverride(name = "modifiedDateTime",column = @Column(name = "MEMBER_INFO_MODIFIED_DTTM")),
        @AttributeOverride(name = "createdDateTime",column = @Column(name = "MEMBER_INFO_CREATED_DTTM")),
        @AttributeOverride(name = "modifiedBy",column = @Column(name = "MEMBER_INFO_MODIFIED_BY",length = 30)),
        @AttributeOverride(name = "createBy",column = @Column(name = "MEMBER_INFO_CREATED_BY",length = 30)),
})
@Entity(name = "MEMBER_INFO")
public class MemberInfo extends BaseEntity {

    /**
     * 회원정보의 ID (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_INFO_PK")
    private Integer id;

    /**
     * 회원정보의 대상 회원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_MEMBER_PK")
    private Member member;

    /**
     * 회원의 휴대폰번호
     */
    @Column(name="MEMBER_INFO_CELL_PHONE_NUMBER",length =15)
    private String phoneNumber;

    /**
     * 회원의 학번
     */
    @Column(name= "MEMBER_INFO_STUDENT_ID",nullable = false,unique = true,length =45)
    private String studentID;

    /**
     * 회원의 기수
     */
    @Column(name="MEMBER_INFO_YEAR",nullable = false)
    private Integer year;

    /**
     * 회원의 회원 등급
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RANK_RANK_PK",nullable = false)
    private Rank rank;

    /**
     * 회원의 전공
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAJOR_MAJOR_PK",nullable = false)
    private Major major;

    /**
     * 회원의 생년월일
     */
    @Column(name="MEMBER_INFO_DATEOFBIRTH")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    public void update(MemberInfo memberInfo){
        if(memberInfo.getPhoneNumber()!=null) this.phoneNumber = memberInfo.getPhoneNumber();
        if(memberInfo.getStudentID()!=null) this.studentID = memberInfo.getStudentID();
        if(memberInfo.getYear()!=null) this.year = memberInfo.getYear();
        if(memberInfo.getRank()!=null) this.rank = memberInfo.getRank();
        if(memberInfo.getMajor()!=null) this.major = memberInfo.getMajor();
        if(memberInfo.getDateOfBirth()!=null) this.dateOfBirth = memberInfo.getDateOfBirth();
        this.modifiedBy = memberInfo.getModifiedBy();
        this.modifiedDateTime = memberInfo.getModifiedDateTime();
    }
}
