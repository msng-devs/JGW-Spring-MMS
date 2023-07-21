package com.jaramgroupware.mms.domain.major;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;



@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "MAJOR")
public class Major {

    /**
     * major의 ID (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAJOR_PK")
    private Long id;

    /**
     * 전공 명칭
     */
    @Column(name = "MAJOR_NM",nullable = false,unique = true,length = 45)
    private String name;

}
