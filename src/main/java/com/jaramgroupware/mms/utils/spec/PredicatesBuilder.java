package com.jaramgroupware.mms.utils.spec;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
@NoArgsConstructor
public class PredicatesBuilder {

    public Predicate build(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<SearchCriteria> list){
        List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();

        //add add criteria to predicates
        for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(builder.greaterThan(
                        root.get(criteria.getKey()), criteria.getValue().get(0).toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(builder.lessThan(
                        root.get(criteria.getKey()), criteria.getValue().get(0).toString()));
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates.add(builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().get(0).toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().get(0).toString()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(builder.notEqual(
                        root.get(criteria.getKey()), criteria.getValue().get(0)));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()), criteria.getValue().get(0)));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().get(0).toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        criteria.getValue().get(0).toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().get(0).toString().toLowerCase()));
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
                predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()).not());
            } else if (criteria.getOperation().equals(SearchOperation.BETWEEN)){
                predicates.add(builder.between(root.<LocalDateTime>get(criteria.getKey()),(LocalDateTime) criteria.getValue().get(0),(LocalDateTime)criteria.getValue().get(1)));
            } else if (criteria.getOperation().equals(SearchOperation.OR_EQUAL)){
                predicates.add(builder.or(builder.equal(
                        root.get(criteria.getKey()), criteria.getValue().get(0))));
            }else if (criteria.getOperation().equals(SearchOperation.BETWEEN_DATE)){
                predicates.add(builder.between(root.<LocalDate>get(criteria.getKey()),(LocalDate) criteria.getValue().get(0),(LocalDate)criteria.getValue().get(1)));
            }
        }

        return builder.and(predicates.toArray(new javax.persistence.criteria.Predicate[0]));
    }
}
