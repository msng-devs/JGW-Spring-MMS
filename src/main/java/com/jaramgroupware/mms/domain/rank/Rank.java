package com.jaramgroupware.mms.domain.rank;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "RANK")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RANK_PK")
    private Integer id;

    @Column(name = "RANK_NM",nullable = false,unique = true,length = 45)
    private String name;



}
