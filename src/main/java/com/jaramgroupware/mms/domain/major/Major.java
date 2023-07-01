package com.jaramgroupware.mms.domain.major;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;



/**
 * 전공에 대한 정보를 담고 있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Getter
@ToString
@EqualsAndHashCode
@Entity(name = "MAJOR")
public class Major {

    /**
     * major의 ID (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAJOR_PK")
    private Integer id;

    /**
     * 전공 명칭
     */
    @Column(name = "MAJOR_NM",nullable = false,unique = true,length = 45)
    private String name;

}
