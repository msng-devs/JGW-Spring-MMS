package com.jaramgroupware.mms.domain.memberInfo;

import com.jaramgroupware.mms.utils.spec.PredicatesBuilder;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class MemberInfoSpecification implements Specification<MemberInfo> {

    private final List<SearchCriteria> list = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<MemberInfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        PredicatesBuilder predicatesBuilder = new PredicatesBuilder();
        query.distinct(true);

        //count query error
        //ref : https://starrybleu.github.io/development/2018/08/10/jpa-n+1-fetch-strategy-specification.html
        if (query.getResultType() != Long.class && query.getResultType() != long.class){
            root.fetch("rank", JoinType.LEFT);
            root.fetch("major", JoinType.LEFT);
        }

        return predicatesBuilder.build(root,query,builder,list);
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }
}
