package com.jaramgroupware.mms.domain.rank;


import jakarta.persistence.*;
import lombok.*;



/**
 * 회원 등급 정보를 담고 있는 클래스
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
@Entity(name = "RANK")
public class Rank {

    /**
     * Rank의 ID (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RANK_PK")
    private Long id;

    /**
     * 회원 등급의 명칭
     */
    @Column(name = "RANK_NM",nullable = false,unique = true,length = 45)
    private String name;



}
