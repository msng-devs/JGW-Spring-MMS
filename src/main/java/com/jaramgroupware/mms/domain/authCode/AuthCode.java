package com.jaramgroupware.mms.domain.authCode;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "AUTH_CODE")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthCode {

    @Id
    @Column(name = "AUTH_CODE_PK", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_INFO_MEMBER_INFO_PK", nullable = false, unique = true)
    private MemberInfo memberInfo;
}
