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

    @Id
    @Column(name = "MEMBER_INFO_PK", nullable = false, length = 28)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "MEMBER_INFO_PK",nullable = false)
    private Member member;

    @Column(name="MEMBER_INFO_CELL_PHONE_NUMBER",length =15)
    private String phoneNumber;

    @Column(name= "MEMBER_INFO_STUDENT_ID",nullable = false,unique = true,length =45)
    private String studentID;

    @Column(name="MEMBER_INFO_YEAR",nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RANK_RANK_PK",nullable = false)
    private Rank rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAJOR_MAJOR_PK",nullable = false)
    private Major major;

    @Column(name="MEMBER_INFO_DATEOFBIRTH")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    public void update(MemberInfo memberInfo,String who){
        phoneNumber = memberInfo.getPhoneNumber();
        major = memberInfo.getMajor();
        rank = memberInfo.getRank();
        year = memberInfo.getYear();
        modifiedDateTime = LocalDateTime.now();
        modifiedBy = who;
    }
}
