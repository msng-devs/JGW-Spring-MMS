package com.jaramgroupware.mms.utils.spec.keys;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 문자열 필드에 한하여 해당 단어를 포함하는 데이터를 찾음.
 * queryParamName : 파싱할 쿼리 파라미터명
 * tableName : 해당 테이블의 컬럼명
 */
@EqualsAndHashCode
@Getter
@ToString
@AllArgsConstructor
public class LikeKey {
    private String queryParamName;
    private String tableName;
}
