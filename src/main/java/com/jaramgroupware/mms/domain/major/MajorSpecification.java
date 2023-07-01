package com.jaramgroupware.mms.domain.major;


import com.jaramgroupware.mms.utils.spec.PredicatesBuilder;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;

/**
 * Major(Object)의 다중 조건 조회를 위한 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
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

    public void set(List<SearchCriteria> newList) {
        list.clear();
        list.addAll(newList);
    }

}
