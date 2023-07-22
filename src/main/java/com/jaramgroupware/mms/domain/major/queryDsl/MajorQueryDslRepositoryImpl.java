package com.jaramgroupware.mms.domain.major.queryDsl;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.QMajor;
import com.jaramgroupware.mms.utils.querydsl.QueryDslRepositoryUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;
@ComponentScan
@RequiredArgsConstructor
@Component
public class MajorQueryDslRepositoryImpl implements MajorQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QueryDslRepositoryUtils<QMajorSortKey, Major> queryDslRepositoryUtils = new QueryDslRepositoryUtils<QMajorSortKey,Major>();

    @Override
    public List<Major> findAllWithQueryParams(Pageable pageable, MultiValueMap<String, String> params) {

        var jpaQuery = queryFactory.selectFrom(QMajor.major);
        applyQueryParams(jpaQuery, params);
        queryDslRepositoryUtils.applyPageable(jpaQuery,pageable);
        return jpaQuery.fetchAll().stream().toList();
    }

    private void applyQueryParams(JPAQuery<Major> jpqQuery, MultiValueMap<String, String> params) {
        if (params.containsKey("name")) {
            for (var value : params.get("name")) {
                jpqQuery.where(QMajor.major.name.like("%" + value + "%"));
            }
        }
    }

}
