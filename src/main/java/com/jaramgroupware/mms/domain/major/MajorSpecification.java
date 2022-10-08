package com.jaramgroupware.mms.domain.major;

import com.jaramgroupware.mms.utils.spec.PredicatesBuilder;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

//ref : https://attacomsian.com/blog/spring-data-jpa-specifications
public class MajorSpecification implements Specification<Major>{

    private final List<SearchCriteria> list = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<Major> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        PredicatesBuilder predicatesBuilder = new PredicatesBuilder();
        return predicatesBuilder.build(root,query,builder,list);
    }


    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

}
