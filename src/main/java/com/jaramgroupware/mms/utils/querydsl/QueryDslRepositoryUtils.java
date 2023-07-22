package com.jaramgroupware.mms.utils.querydsl;

import com.jaramgroupware.mms.domain.QSortKey;
import com.jaramgroupware.mms.domain.major.Major;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;

@NoArgsConstructor
public class QueryDslRepositoryUtils<T extends QSortKey<?>,M> {


    public void applyPageable(JPAQuery<M> jpaQuery, Pageable pageable) {
        applySort(jpaQuery, pageable.getSort());
        jpaQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());
    }

    private void applySort(JPAQuery<M> jpaQuery, Sort sort) {
        for (Sort.Order order : sort) {
            jpaQuery.orderBy(T.getOrderSpecifier(order.getProperty(), order.getDirection()));
        }
    }
}
