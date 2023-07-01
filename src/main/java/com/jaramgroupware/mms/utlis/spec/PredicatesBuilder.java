package com.jaramgroupware.mms.utlis.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
@NoArgsConstructor
public class PredicatesBuilder {

    public Predicate build(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<SearchCriteria> list){
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : list) {
            predicates.add(getPredicate(root, builder, criteria));
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate getPredicate(Root<?> root, CriteriaBuilder builder, SearchCriteria criteria) {
        return switch (criteria.getOperation()) {
            case GREATER_THAN ->
                    builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().get(0).toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().get(0).toString());
            case GREATER_THAN_EQUAL ->
                    builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().get(0).toString());
            case LESS_THAN_EQUAL ->
                    builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().get(0).toString());
            case NOT_EQUAL -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue().get(0));
            case EQUAL -> builder.equal(root.get(criteria.getKey()), criteria.getValue().get(0));
            case MATCH ->
                    builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().get(0).toString().toLowerCase() + "%");
            case MATCH_END ->
                    builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().get(0).toString().toLowerCase() + "%");
            case MATCH_START ->
                    builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().get(0).toString().toLowerCase());
            case IN -> builder.in(root.get(criteria.getKey())).value(criteria.getValue());
            case NOT_IN -> builder.in(root.get(criteria.getKey())).value(criteria.getValue()).not();
            case BETWEEN ->
                    builder.between(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue().get(0), (LocalDateTime) criteria.getValue().get(1));
            case OR_EQUAL -> builder.or(builder.equal(root.get(criteria.getKey()), criteria.getValue().get(0)));
            case BETWEEN_DATE ->
                    builder.between(root.get(criteria.getKey()), (LocalDate) criteria.getValue().get(0), (LocalDate) criteria.getValue().get(1));
            default -> throw new RuntimeException("Operation not supported. ");
        };
    }
}
