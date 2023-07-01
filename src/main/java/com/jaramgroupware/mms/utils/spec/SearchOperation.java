package com.jaramgroupware.mms.utils.spec;

//ref : https://attacomsian.com/blog/spring-data-jpa-specifications
public enum SearchOperation {
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN_EQUAL,
    NOT_EQUAL,
    EQUAL,
    MATCH,
    MATCH_START,
    MATCH_END,
    IN,
    NOT_IN,
    BETWEEN,
    OR_EQUAL,
    BETWEEN_DATE
}
