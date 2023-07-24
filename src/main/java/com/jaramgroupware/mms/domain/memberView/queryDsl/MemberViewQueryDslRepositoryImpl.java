package com.jaramgroupware.mms.domain.memberView.queryDsl;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.QMember;
import com.jaramgroupware.mms.domain.memberView.MemberView;
import com.jaramgroupware.mms.domain.memberView.QMemberView;
import com.jaramgroupware.mms.utils.querydsl.QueryDslRepositoryUtils;
import com.jaramgroupware.mms.utils.querydsl.keys.EqualKey;
import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.jaramgroupware.mms.utils.parse.ParseByNameType.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@ComponentScan
@RequiredArgsConstructor
@Component
public class MemberViewQueryDslRepositoryImpl implements MemberViewQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QueryDslRepositoryUtils<MemberView> queryDslRepositoryUtils = new QueryDslRepositoryUtils<MemberView>();
    private final QMemberViewSortKey qMemberViewSortKey = new QMemberViewSortKey();
    private final QMemberView qMemberView = QMemberView.memberView;


//    private final Map<String, Path<Integer>> integerEqualKeys = Map.of(
//            "year", qMemberView.year
//    );
//
//    private final Map<String, Path<Long>> longEqualKeys = Map.of(
//            "role", qMemberView.role,
//            "rank", qMemberView.rank,
//            "major", qMemberView.major
//    );

//    private final Map<String, Path<Boolean>> booleanEqualKeys = Map.of(
//            "status", qMemberView.status,
//            "isLeaveAbsence", qMemberView.isLeaveAbsence
//    );

    @Override
    public List<MemberView> findAllWithQueryParams(Pageable pageable, MultiValueMap<String, String> params) {
        var jpaQuery = queryFactory.selectFrom(qMemberView);
        applyQueryParams(jpaQuery, params);
        queryDslRepositoryUtils.applyPageable(jpaQuery, pageable, qMemberViewSortKey);
        return jpaQuery.fetch().stream().toList();
    }

    private void applyQueryParams(JPAQuery<MemberView> jpqQuery, MultiValueMap<String, String> params) {

        if (params.isEmpty()) return;

        var whereBase = new HashSet<BooleanExpression>();

        //integer keys
        if (params.containsKey("year")) {
            for (var value : params.get("year")) {
                whereBase.add(qMemberView.year.eq(parseInt(value)));
            }
        }

        //long keys
        if (params.containsKey("role")) {
            for (var value : params.get("role")) {
                whereBase.add(qMemberView.role.eq(parseLong(value)));
            }
        }
        if (params.containsKey("rank")) {
            for (var value : params.get("rank")) {
                whereBase.add(qMemberView.rank.eq(parseLong(value)));
            }
        }
        if (params.containsKey("major")) {
            for (var value : params.get("major")) {
                whereBase.add(qMemberView.major.eq(parseLong(value)));
            }
        }

        //boolean keys
        if (params.containsKey("status")) {
            for (var value : params.get("status")) {
                whereBase.add(qMemberView.status.eq(parseBoolean(value)));
            }
        }
        if (params.containsKey("isLeaveAbsence")) {
            for (var value : params.get("isLeaveAbsence")) {
                whereBase.add(qMemberView.isLeaveAbsence.eq(parseBoolean(value)));
            }
        }

        //like keys
        if (params.containsKey("name")) {
            for (var value : params.get("name")) {
                whereBase.add(qMemberView.name.like(value));
            }
        }

        if (params.containsKey("email")) {
            for (var value : params.get("email")) {
                whereBase.add(qMemberView.email.like(value));
            }
        }

        if (params.containsKey("studentId")) {
            for (var value : params.get("studentId")) {
                whereBase.add(qMemberView.studentId.like(value));
            }
        }

        var combineWhere = whereBase.stream().reduce(BooleanExpression::and).orElse(null);

        jpqQuery.where(combineWhere);
    }

}
