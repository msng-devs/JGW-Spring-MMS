package com.jaramgroupware.mms.domain.major;


import com.jaramgroupware.mms.utils.spec.SpecificationBuilder;
import com.jaramgroupware.mms.utils.spec.keys.LikeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

/**
 * Major(Object)의 다중 조건 조회를 위한 Builder 클래스
 *
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 * @since 2023-03-07
 */
@Component
@RequiredArgsConstructor
public class MajorSpecificationBuilder {

    private final SpecificationBuilder specificationBuilder;

    private final List<LikeKey> likeKeys = List.of(
            new LikeKey("name", "name")
    );

    public MajorSpecification toSpec(MultiValueMap<String, String> queryParam) {


        var specification = new MajorSpecification();

        if(queryParam.isEmpty()) return specification;

        var spec = specificationBuilder.buildSearchCriteria(
                Collections.emptyList(),
                Collections.emptyList(),
                likeKeys,
                Collections.emptyList(),
                queryParam
        );

        specification.set(spec);

        return specification;
    }

}

