//package com.jaramgroupware.mms.domain.preMemberInfo;
//
//import com.jaramgroupware.mms.utils.spec.SpecificationBuilder;
//import com.jaramgroupware.mms.utils.querydsl.keys.EqualKey;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//
//import java.util.Collections;
//import java.util.List;
//
//import static com.jaramgroupware.mms.utils.parse.ParseByNameType.*;
//
//@Component
//@RequiredArgsConstructor
//public class PreMemberInfoSpecificationBuilder {
//
//    private final SpecificationBuilder specificationBuilder;
//
//    private final List<EqualKey> equalKeys = List.of(
//            new EqualKey("name", "name",STRING),
//            new EqualKey("role", "email",LONG),
//            new EqualKey("studentId","studentId",STRING),
//            new EqualKey("year","year",INTEGER),
//            new EqualKey("rank","rank",LONG),
//            new EqualKey("major","major",LONG)
//    );
//
//
//    public PreMemberInfoSpecification toSpec(MultiValueMap<String, String> queryParam) {
//
//
//        var specification = new PreMemberInfoSpecification();
//
//        if(queryParam.isEmpty()) return specification;
//
//        var spec = specificationBuilder.buildSearchCriteria(
//                Collections.emptyList(),
//                equalKeys,
//                Collections.emptyList(),
//                Collections.emptyList(),
//                queryParam
//        );
//
//        specification.addAll(spec);
//
//        return specification;
//    }
//}
