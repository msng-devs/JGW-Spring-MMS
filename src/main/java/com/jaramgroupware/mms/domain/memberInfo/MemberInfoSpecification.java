package com.jaramgroupware.mms.domain.memberInfo;


import com.jaramgroupware.mms.utlis.spec.PredicatesBuilder;
import com.jaramgroupware.mms.utlis.spec.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * MemberInfo(Object)의 다중 조건 조회를 위한 클래스
 *
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 * @since 2023-03-07
 */
public class MemberInfoSpecification implements Specification<MemberInfo> {

    private final List<SearchCriteria> list = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<MemberInfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        PredicatesBuilder predicatesBuilder = new PredicatesBuilder();
        query.distinct(true);

        //count query error
        //ref : https://starrybleu.github.io/development/2018/08/10/jpa-n+1-fetch-strategy-specification.html
        if (query.getResultType() != Long.class && query.getResultType() != long.class) {
            root.fetch("rank", JoinType.LEFT);
            root.fetch("major", JoinType.LEFT);
        }

        return predicatesBuilder.build(root, query, builder, list);
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    public void set(List<SearchCriteria> list) {
        this.list.addAll(list);
    }
}
