package com.jaramgroupware.mms.domain.major.queryDsl;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.QMajor;
import com.jaramgroupware.mms.utils.querydsl.QueryDslRepositoryUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Slf4j
@ComponentScan
@RequiredArgsConstructor
@Component
public class MajorQueryDslRepositoryImpl implements MajorQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QueryDslRepositoryUtils<Major> queryDslRepositoryUtils = new QueryDslRepositoryUtils<Major>();
    private final QMajorSortKey qMajorSortKey = new QMajorSortKey();
    @Override
    public List<Major> findAllWithQueryParams(Pageable pageable, MultiValueMap<String, String> params) {

        var jpaQuery = queryFactory.selectFrom(QMajor.major);
        applyQueryParams(jpaQuery, params);
        queryDslRepositoryUtils.applyPageable(jpaQuery,pageable,qMajorSortKey);
        return jpaQuery.fetch().stream().toList();
    }

    @Override
    public Integer countAllWithQueryParams(MultiValueMap<String, String> params) {
        var jpaQuery = queryFactory.selectFrom(QMajor.major);
        applyQueryParams(jpaQuery, params);
        return jpaQuery.fetch().size();
    }

    private void applyQueryParams(JPAQuery<Major> jpqQuery, MultiValueMap<String, String> params) {
        if (params.containsKey("name")) {
            var value = params.getFirst("name");
            jpqQuery.where(QMajor.major.name.like("%" + value + "%"));
        }
    }

}
