package com.jaramgroupware.mms.domain.memberView;

import com.jaramgroupware.mms.domain.major.MajorSpecification;
import com.jaramgroupware.mms.utils.spec.SpecificationBuilder;
import com.jaramgroupware.mms.utils.spec.keys.DateRangeKey;
import com.jaramgroupware.mms.utils.spec.keys.EqualKey;
import com.jaramgroupware.mms.utils.spec.keys.LikeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.jaramgroupware.mms.utils.parse.ParseByNameType.*;

@Component
@RequiredArgsConstructor
public class MemberViewSpecificationBuilder {

    private final SpecificationBuilder specificationBuilder;

    private final List<EqualKey> equalKeys = List.of(
            new EqualKey("name", "name",STRING),
            new EqualKey("email", "email",STRING),
            new EqualKey("role", "email",LONG),
            new EqualKey("status","status",BOOLEAN),
            new EqualKey("phoneNumber","cellPhoneNumber",LONG),
            new EqualKey("studentId","studentId",STRING),
            new EqualKey("year","year",INTEGER),
            new EqualKey("rank","rank",LONG),
            new EqualKey("major","major",LONG),
            new EqualKey("isLeaveAbsence","isLeaveAbsence",BOOLEAN)
    );

    private final List<LikeKey> likeKeys = List.of(
            new LikeKey("majorName", "majorName")
    );

    private final List<DateRangeKey> dateRangeKeys = List.of(
            new DateRangeKey("startDateOfBirth", "endDateOfBirth", "dateOfBirth")
    );

    public MemberViewSpecification toSpec(MultiValueMap<String, String> queryParam) {


        var specification = new MemberViewSpecification();

        if(queryParam.isEmpty()) return specification;

        var spec = specificationBuilder.buildSearchCriteria(
                dateRangeKeys,
                equalKeys,
                likeKeys,
                Collections.emptyList(),
                queryParam
        );

        specification.addAll(spec);

        return specification;
    }
}
