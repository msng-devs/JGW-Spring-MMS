package com.jaramgroupware.mms.domain.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.BaseEntity;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
@Entity(name = "MEMBER")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Member {

    @Id
    @Column(name = "MEMBER_PK",nullable = false,length = 28)
    private String id;

    @Column(name = "MEMBER_NM", nullable = false, length = 45)
    private String name;

    @Email
    @Column(name = "MEMBER_EMAIL",nullable = false, unique = true, length = 255)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ROLE_PK",nullable = false)
    private Role role;

    @ColumnDefault("1")
    @Column(name="MEMBER_STATUS",nullable = false)
    private boolean status;

    public void update(Member member,String who){
        name = member.getName();
        role = member.getRole();
        status = member.isStatus();
    }
}
