package com.jaramgroupware.mms.domain.config;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "CONFIG")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONFIG_PK")
    private Integer id;

    @Column(name = "CONFIG_NM",nullable = false,unique = true,length = 50)
    private String name;

    @Column(name = "CONFIG_VAL",nullable = false,length = 50)
    private String val;

}
