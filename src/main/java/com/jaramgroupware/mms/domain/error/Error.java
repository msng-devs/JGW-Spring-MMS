package com.jaramgroupware.mms.domain.error;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ERROR")
public class Error {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ERROR_PK")
    private Integer id;

    @Column(name = "ERROR_NM",nullable = false,unique = true,length = 50)
    private String name;

    @Column(name = "ERROR_TITLE",length = 50)
    private String title;

    @Column(name = "ERROR_HTTP_CODE",length = 3)
    private String code;

    @Column(name = "ERROR_INDEX")
    private String index;


}
