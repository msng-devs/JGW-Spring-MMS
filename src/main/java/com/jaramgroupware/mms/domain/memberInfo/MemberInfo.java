package com.jaramgroupware.mms.domain.memberInfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.BaseEntity;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.rank.Rank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원정보를 담고 있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
    @JoinColumn(name = "MEMBER_MEMBER_PK",nullable = false)
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

    /**
     * MemberInfo 객체의 업데이트를 위한 함수
     * @param memberInfo 업데이트 하고자 하는 MemberInfo(Object)
     * @param who 업데이트 요청한 Member(Object)의 ID
     */
    public void update(MemberInfo memberInfo,String who){
        phoneNumber = memberInfo.getPhoneNumber();
        major = memberInfo.getMajor();
        rank = memberInfo.getRank();
        year = memberInfo.getYear();
        modifiedDateTime = LocalDateTime.now();
        modifiedBy = who;
    }
}
