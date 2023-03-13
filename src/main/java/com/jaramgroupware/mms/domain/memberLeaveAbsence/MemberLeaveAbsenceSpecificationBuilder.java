package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import com.jaramgroupware.mms.utils.spec.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

/**
 * MemberLeaveAbsence(Object)의 다중 조건 조회를 위한 Builder 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Component
@RequiredArgsConstructor
public class MemberLeaveAbsenceSpecificationBuilder {

    private final ParseByNameBuilder parseByNameBuilder;

    private final LocalDate maxDate = LocalDate.parse("9999-12-31",DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private final LocalDate minDate = LocalDate.parse("0001-01-01",DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    /**
     * 해당 옵션들은 입력된 값과 완전히 일치 되는 경우를 탐색한다
     */
    @Getter
    @AllArgsConstructor
    private enum EqualKeys {

        //        MEMBER("memberID","member","String"),
        STATUS("status","status","Boolean");

        private final String queryParamName;
        private final String tableName;
        private final String type;
    }

    /**
     * 해당 옵션들을 사용해서 날짜 범위를 탐색한다
     */
    @Getter
    @AllArgsConstructor
    private enum DateRangeKeys {

        EXPECTEDDATERETURNSCHOOL("startExpectedDateOfReturnSchool","endExpectedDateOfReturnSchool","expectedDateReturnSchool");

        private final String startQueryParamName;
        private final String endQueryParamName;
        private final String tableName;
    }

    public MemberLeaveAbsenceSpecification toSpec(MultiValueMap<String, String> queryParam){

        MemberLeaveAbsenceSpecification specification = new MemberLeaveAbsenceSpecification();

        for (MemberLeaveAbsenceSpecificationBuilder.EqualKeys key: MemberLeaveAbsenceSpecificationBuilder.EqualKeys.values()) {
            if(queryParam.containsKey(key.getQueryParamName())){
                specification.add(new SearchCriteria(key.getTableName()
                        , Collections.singletonList(parseByNameBuilder.parse(queryParam.getFirst(key.getQueryParamName()), key.getType()))
                        , SearchOperation.EQUAL));
            }
        }

        for (MemberLeaveAbsenceSpecificationBuilder.DateRangeKeys key: MemberLeaveAbsenceSpecificationBuilder.DateRangeKeys.values()) {
            if(queryParam.containsKey(key.getStartQueryParamName()) || queryParam.containsKey(key.getEndQueryParamName()) ){
                LocalDate start = minDate;
                LocalDate end = maxDate;
                try {
                    start = (queryParam.containsKey(key.getStartQueryParamName()))
                            ? LocalDate.parse(queryParam.getFirst(key.getStartQueryParamName()), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            : minDate;
                    end = (queryParam.containsKey(key.getEndQueryParamName()))
                            ? LocalDate.parse(queryParam.getFirst(key.getEndQueryParamName()),DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            : maxDate;
                }catch (Exception e){
                    throw new IllegalArgumentException(key.getStartQueryParamName()+"혹은"+key.getEndQueryParamName()+"의 형식이 잘못됬습니다.");
                }

                if(start.isAfter(end)){
                    throw new IllegalArgumentException(key.getStartQueryParamName()+"과"+ key.getStartQueryParamName()+"인자가 잘못됬습니다. 범위가 적절한지 다시 확인하세요.");
                }
                specification.add(new SearchCriteria(key.getTableName(),
                        Arrays.asList(new LocalDate[] {start,end}),
                        SearchOperation.BETWEEN_DATE
                ));
            }
        }
        return specification;
    }
}
