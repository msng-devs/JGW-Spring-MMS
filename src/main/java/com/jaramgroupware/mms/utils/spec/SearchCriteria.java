package com.jaramgroupware.mms.utils.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//ref : https://attacomsian.com/blog/spring-data-jpa-specifications
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private List<Object> value;
    private SearchOperation operation;
}
