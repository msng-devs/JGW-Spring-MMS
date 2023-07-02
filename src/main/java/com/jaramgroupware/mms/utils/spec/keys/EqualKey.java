package com.jaramgroupware.mms.utils.spec.keys;

import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
import com.jaramgroupware.mms.utils.parse.ParseByNameType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 해당 필드가 쿼리라파미터의 내용과 같은지 확인하는 키 종류
 * queryParamName : 파싱할 쿼리 파라미터명
 * tableName : 해당 테이블의 컬럼명
 * type : 해당 컬럼의 데이터 타입
 */
@EqualsAndHashCode
@Getter
@ToString
@AllArgsConstructor
public class EqualKey {
    private String queryParamName;
    private String tableName;
    private ParseByNameType type;
}
