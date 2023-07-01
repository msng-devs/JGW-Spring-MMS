package com.jaramgroupware.mms.domain.member;


import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import com.jaramgroupware.mms.utils.spec.SearchOperation;
import com.jaramgroupware.mms.utils.spec.SpecificationBuilder;
import com.jaramgroupware.mms.utils.spec.keys.EqualKey;
import com.jaramgroupware.mms.utils.spec.keys.LikeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Member(Object)의 다중 조건 조회를 위한 Builder 클래스
 *
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 * @since 2023-03-07
 */
@Component
@RequiredArgsConstructor
public class MemberSpecificationBuilder {
    private final SpecificationBuilder specificationBuilder;

    private final Role guestRole = Role.builder().id(1).build();
    private final Rank guestRank = Rank.builder().id(1).build();


    private final List<EqualKey> equalKeys = Arrays.asList(
            new EqualKey("roleID", "role", "Integer"),
            new EqualKey("status", "status", "Boolean")
    );

    private final List<LikeKey> likeKeys = Arrays.asList(
            new LikeKey("email", "email"),
            new LikeKey("name", "name")
    );

    public MemberSpecification toSpec(MultiValueMap<String, String> queryParam) {


        var specification = new MemberSpecification();

        boolean isIncludeGuest = Boolean.parseBoolean(queryParam.getFirst("includeGuest"));

        if (!isIncludeGuest) {
            specification.add(
                    new SearchCriteria("role",
                            Arrays.asList(guestRole, guestRank),
                            SearchOperation.NOT_IN)
            );
        } else {
            specification.add(
                    new SearchCriteria("role",
                            Arrays.asList(guestRole, guestRank),
                            SearchOperation.IN)
            );
        }

        var spec = specificationBuilder.buildSearchCriteria(
                Collections.emptyList(),
                equalKeys,
                likeKeys,
                Collections.emptyList(),
                queryParam
        );

        specification.set(spec);


        return specification;
    }

}

