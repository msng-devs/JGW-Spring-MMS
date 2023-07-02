//package com.jaramgroupware.mms.domain.memberInfo;
//
//import com.jaramgroupware.mms.utils.spec.SpecificationBuilder;
//import com.jaramgroupware.mms.utils.spec.keys.DateRangeKey;
//import com.jaramgroupware.mms.utils.spec.keys.DateTimeRangeKey;
//import com.jaramgroupware.mms.utils.spec.keys.EqualKey;
//import com.jaramgroupware.mms.utils.spec.keys.LikeKey;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * MemberInfo(Object)의 다중 조건 조회를 위한 Builder 클래스
// *
// * @author 황준서(37기) hzser123@gmail.com
// * @author 이현희(38기) heeit13145@gmail.com
// * @since 2023-03-07
// */
//@Component
//@RequiredArgsConstructor
//public class MemberInfoSpecificationBuilder {
//    private final SpecificationBuilder specificationBuilder;
//
//    private final List<EqualKey> equalKeys = Arrays.asList(
//            new EqualKey("majorID", "major", "Integer"),
//            new EqualKey("rankID", "rank", "Integer"),
//            new EqualKey("year", "year", "Integer"),
//            new EqualKey("createBy", "createBy", "String"),
//            new EqualKey("modifiedBy", "modifiedBy", "String")
//    );
//
//    private final List<DateTimeRangeKey> dateTimeRangeKeys = Arrays.asList(
//            new DateTimeRangeKey("startCreatedDateTime", "endCreatedDateTime", "createdDateTime"),
//            new DateTimeRangeKey("startModifiedDateTime", "endModifiedDateTime", "modifiedDateTime")
//    );
//
//    private final List<DateRangeKey> dateRangeKeys = List.of(
//            new DateRangeKey("startDateOfBirth", "endDateOfBirth", "dateOfBirth")
//    );
//
//    private final List<LikeKey> likeKeys = Arrays.asList(
//            new LikeKey("phoneNumber", "phoneNumber"),
//            new LikeKey("studentID", "studentID")
//    );
//
//    public MemberInfoSpecification toSpec(MultiValueMap<String, String> queryParam) {
//
//
//        var specification = new MemberInfoSpecification();
//
//        var spec = specificationBuilder.buildSearchCriteria(
//                dateRangeKeys,
//                equalKeys,
//                likeKeys,
//                dateTimeRangeKeys,
//                queryParam
//        );
//
//        specification.set(spec);
//
//        return specification;
//    }
//
//}
