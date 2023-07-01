package com.jaramgroupware.mms.domain.role;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;



/**
 * 권한 레벨 정보를 담고 있는 클래스
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
@Entity(name = "ROLE")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Role {

    /**
     * Role의 ID (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_PK")
    private Integer id;

    /**
     * 권한 레벨의 명칭
     */
    @Column(name = "ROLE_NM",nullable = false,unique = true,length = 45)
    private String name;


}
