//package com.jaramgroupware.mms.domain.memberView;
//
//import com.jaramgroupware.mms.domain.member.Member;
//import com.jaramgroupware.mms.utils.spec.PredicatesBuilder;
//import com.jaramgroupware.mms.utils.spec.SearchCriteria;
//import jakarta.persistence.criteria.*;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MemberViewSpecification implements Specification<MemberView> {
//
//    private final List<SearchCriteria> list = new ArrayList<>();
//
//    @Override
//    public Predicate toPredicate(Root<MemberView> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//        var predicatesBuilder = new PredicatesBuilder();
//        query.distinct(true);
//
//        return predicatesBuilder.build(root, query, builder, list);
//    }
//
//    public void add(SearchCriteria criteria) {
//        list.add(criteria);
//    }
//
//    public void addAll(List<SearchCriteria> newList) {
//        list.addAll(newList);
//    }
//}
