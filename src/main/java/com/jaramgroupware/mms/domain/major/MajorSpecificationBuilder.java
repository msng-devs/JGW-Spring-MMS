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

@Component
@RequiredArgsConstructor
public class MajorSpecificationBuilder {

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

