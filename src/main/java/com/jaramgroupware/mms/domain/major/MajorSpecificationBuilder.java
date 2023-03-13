package com.jaramgroupware.mms.domain.major;

import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import com.jaramgroupware.mms.utils.spec.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

/**
 * Major(Object)의 다중 조건 조회를 위한 Builder 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Component
@RequiredArgsConstructor
public class MajorSpecificationBuilder {

    /**
     * 해당 옵션들은 해당 문자열을 포함하는 경우를 탐색한다
     */
    @Getter
    @AllArgsConstructor
    private enum LikeKeys{

        NAME("name","name");

        private final String queryParamName;
        private final String tableName;
    }

    public MajorSpecification toSpec(MultiValueMap<String, String> queryParam){


        MajorSpecification specification = new MajorSpecification();

            for (LikeKeys key : LikeKeys.values()) {
                if(queryParam.containsKey(key.getQueryParamName())){
                    specification.add(new SearchCriteria(key.getTableName()
                            , Arrays.asList(new String[]{queryParam.getFirst(key.getQueryParamName())})
                            ,SearchOperation.MATCH));
                }
            }

            return specification;
    }

}

