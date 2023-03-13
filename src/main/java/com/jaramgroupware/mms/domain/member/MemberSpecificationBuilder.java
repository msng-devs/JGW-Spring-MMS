package com.jaramgroupware.mms.domain.member;

import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import com.jaramgroupware.mms.utils.spec.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Member(Object)의 다중 조건 조회를 위한 Builder 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Component
@RequiredArgsConstructor
public class MemberSpecificationBuilder {
    private final ParseByNameBuilder parseByNameBuilder;

    private final Role defaultMemberRole = Role.builder().id(1).build();
    private final Role defaultNewMemberRole = Role.builder().id(0).build();

    private final Rank defaultRank = Rank.builder().id(1).build();
    private final Rank defaultNewRank = Rank.builder().id(2).build();

    /**
     * 해당 옵션들은 입력된 값과 완전히 일치 되는 경우를 탐색한다
     */
    @Getter
    @AllArgsConstructor
    private enum EqualKeys {

        ROLE("roleID","role","Integer"),
        STATUS("status","status","Boolean");

        private final String queryParamName;
        private final String tableName;
        private final String type;
    }

    /**
     * 해당 옵션들은 해당 문자열을 포함하는 경우를 탐색한다
     */
    @Getter
    @AllArgsConstructor
    private enum LikeKeys{

        EMAIL("email","email"),
        NAME("name","name");

        private final String queryParamName;
        private final String tableName;
    }
    public MemberSpecification toSpec(MultiValueMap<String, String> queryParam){


        MemberSpecification specification = new MemberSpecification();
        boolean isIncludeGuest = Boolean.parseBoolean(queryParam.getFirst("includeGuest"));

        if(!isIncludeGuest){
            specification.add(
                    new SearchCriteria("role",
                            Arrays.asList(defaultMemberRole,defaultNewMemberRole),
                            SearchOperation.NOT_IN)
            );
        }else{
            specification.add(
                    new SearchCriteria("role",
                            Arrays.asList(defaultMemberRole,defaultNewMemberRole),
                            SearchOperation.IN)
            );
        }
        
            for (EqualKeys key: EqualKeys.values()) {
                if(queryParam.containsKey(key.getQueryParamName())){
                    specification.add(new SearchCriteria(key.getTableName()
                            , Collections.singletonList(parseByNameBuilder.parse(queryParam.getFirst(key.getQueryParamName()), key.getType()))
                            ,SearchOperation.EQUAL));
                }
            }

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

