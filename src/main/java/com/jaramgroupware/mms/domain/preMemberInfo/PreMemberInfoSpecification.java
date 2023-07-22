//package com.jaramgroupware.mms.domain.preMemberInfo;
//
//import com.jaramgroupware.mms.domain.memberView.MemberView;
//import com.jaramgroupware.mms.utils.spec.PredicatesBuilder;
//import com.jaramgroupware.mms.utils.spec.SearchCriteria;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PreMemberInfoSpecification implements Specification<PreMemberInfo> {
//
//    private final List<SearchCriteria> list = new ArrayList<>();
//
//    @Override
//    public Predicate toPredicate(Root<PreMemberInfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
