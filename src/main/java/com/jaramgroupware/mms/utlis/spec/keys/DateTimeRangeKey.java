package com.jaramgroupware.mms.utlis.spec.keys;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * DataTime 형식의 데이터의 쿼리 파라미터를 파싱할 수 있는 클래스
 * startQueryParamName ~ endQueryParamName 범위에 있는지 조사함.
 * startQueryParamName : 검색할 날짜 시작 범위에 해당 하는 쿼리 파라미터명. 없을 경우 최소 Date
 * endQueryParamName : 검색할 날찌 끝 범위에 해당하는 쿼리 파라미터명. 없을 경우 최대 Date
 * tableName : 해당 테이블의 컬럼명
 */
@EqualsAndHashCode
@Getter
@ToString
@AllArgsConstructor
public class DateTimeRangeKey {
    private String startQueryParamName;
    private String endQueryParamName;
    private String tableName;
}
