package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import com.jaramgroupware.mms.utils.spec.PredicatesBuilder;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MemberLeaveAbsence(Object)의 다중 조건 조회를 위한 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
public class MemberLeaveAbsenceSpecification implements Specification<MemberLeaveAbsence> {
    private final List<SearchCriteria> list = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<MemberLeaveAbsence> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        PredicatesBuilder predicatesBuilder = new PredicatesBuilder();
        query.distinct(true);

        //count query error
        //ref : https://starrybleu.github.io/development/2018/08/10/jpa-n+1-fetch-strategy-specification.html
//        if (query.getResultType() != Long.class && query.getResultType() != long.class){
//            root.fetch("member", JoinType.LEFT);
//        }

        return predicatesBuilder.build(root,query,builder,list);
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }
}
