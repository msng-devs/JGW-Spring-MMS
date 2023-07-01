package com.jaramgroupware.mms.domain.memberLeaveAbsence;


import com.jaramgroupware.mms.utlis.spec.SearchCriteria;
import com.jaramgroupware.mms.utlis.spec.SearchOperation;
import com.jaramgroupware.mms.utlis.spec.SpecificationBuilder;
import com.jaramgroupware.mms.utlis.spec.keys.DateRangeKey;
import com.jaramgroupware.mms.utlis.spec.keys.EqualKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MemberLeaveAbsence(Object)의 다중 조건 조회를 위한 Builder 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Component
@RequiredArgsConstructor
public class MemberLeaveAbsenceSpecificationBuilder {

    private final SpecificationBuilder specificationBuilder;

    private final List<EqualKey> equalKeys = List.of(
            new EqualKey("status","status","Boolean")
    );
    private final List<DateRangeKey> dateRangeKeys = List.of(
            new DateRangeKey("startExpectedDateOfReturnSchool","endExpectedDateOfReturnSchool","expectedDateReturnSchool")
    );

    public MemberLeaveAbsenceSpecification toSpec(MultiValueMap<String, String> queryParam){

        var specification = new MemberLeaveAbsenceSpecification();
        var spec = specificationBuilder.buildSearchCriteria(
                dateRangeKeys,
                equalKeys,
                Collections.emptyList(),
                Collections.emptyList(),
                queryParam
        );
        return specification;
    }
}
