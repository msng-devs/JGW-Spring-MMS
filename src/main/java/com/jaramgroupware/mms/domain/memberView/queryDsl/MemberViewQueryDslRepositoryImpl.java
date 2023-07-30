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
            var yearBase = new HashSet<BooleanExpression>();
            for (var value : params.get("year")) {
                yearBase.add(qMemberView.year.eq(parseInt(value)));
            }
            whereBase.add(yearBase.stream().reduce(BooleanExpression::or).orElse(null));
        }

        //long keys
        if (params.containsKey("role")) {
            var roleBase = new HashSet<BooleanExpression>();
            for (var value : params.get("role")) {
                roleBase.add(qMemberView.role.eq(parseLong(value)));
            }
            whereBase.add(roleBase.stream().reduce(BooleanExpression::or).orElse(null));
        }
        if (params.containsKey("rank")) {
            var rankBase = new HashSet<BooleanExpression>();
            for (var value : params.get("rank")) {
                rankBase.add(qMemberView.rank.eq(parseLong(value)));
            }
            whereBase.add(rankBase.stream().reduce(BooleanExpression::or).orElse(null));
        }
        if (params.containsKey("major")) {
            var majorBase = new HashSet<BooleanExpression>();
            for (var value : params.get("major")) {
                majorBase.add(qMemberView.major.eq(parseLong(value)));
            }
            whereBase.add(majorBase.stream().reduce(BooleanExpression::or).orElse(null));
        }

        //boolean keys
        if (params.containsKey("status")) {
            var value = params.getFirst("status");
            whereBase.add(qMemberView.status.eq(parseBoolean(value)));
        }
        if (params.containsKey("isLeaveAbsence")) {
            var value = params.getFirst("isLeaveAbsence");
            whereBase.add(qMemberView.status.eq(parseBoolean(value)));
        }

        //like keys
        if (params.containsKey("name")) {
            var value = params.getFirst("name");
            whereBase.add(qMemberView.name.like("%"+value+"%"));
        }

        if (params.containsKey("email")) {
            for (var value : params.get("email")) {
                whereBase.add(qMemberView.email.like("%"+value+"%"));
            }
        }

        if (params.containsKey("studentId")) {
            for (var value : params.get("studentId")) {
                whereBase.add(qMemberView.studentId.like("%"+value+"%"));
            }
        }

        var combineWhere = whereBase.stream().reduce(BooleanExpression::and).orElse(null);

        jpqQuery.where(combineWhere);
    }

}
