package com.jaramgroupware.mms.domain.preMemberInfo.queryDsl;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.QPreMemberInfo;
import com.jaramgroupware.mms.utils.querydsl.QueryDslRepositoryUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@ComponentScan
@RequiredArgsConstructor
@Component
public class PreMemberInfoQueryDslRepositoryImpl implements PreMemberInfoQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QueryDslRepositoryUtils<PreMemberInfo> queryDslRepositoryUtils = new QueryDslRepositoryUtils<PreMemberInfo>();
    private final QPreMemberInfoSortKey qPreMemberInfoSortKey = new QPreMemberInfoSortKey();

    @Override
    public List<PreMemberInfo> findAllWithQueryParams(Pageable pageable, MultiValueMap<String, String> params) {

        var jpaQuery = queryFactory.selectFrom(QPreMemberInfo.preMemberInfo);
        applyQueryParams(jpaQuery, params);
        queryDslRepositoryUtils.applyPageable(jpaQuery,pageable,qPreMemberInfoSortKey);

        return jpaQuery.fetch().stream().toList();
    }

    private void applyQueryParams(JPAQuery<PreMemberInfo> jpqQuery, MultiValueMap<String, String> params) {

        if(params.isEmpty()) return;

        var whereBase = new HashSet<BooleanExpression>();

        //equal keys
        if (params.containsKey("role")){
            var roleBase = new HashSet<BooleanExpression>();
            for (var value : params.get("role")) {
                roleBase.add(QPreMemberInfo.preMemberInfo.role.id.eq(parseLong(value)));
            }
            whereBase.add(roleBase.stream().reduce(BooleanExpression::or).orElse(null));
        }

        if (params.containsKey("major")){
            var majorBase = new HashSet<BooleanExpression>();
            for (var value : params.get("major")) {
                majorBase.add(QPreMemberInfo.preMemberInfo.major.id.eq(parseLong(value)));
            }
            whereBase.add(majorBase.stream().reduce(BooleanExpression::or).orElse(null));
        }

        if (params.containsKey("year")){
            var yearBase = new HashSet<BooleanExpression>();
            for (var value : params.get("year")) {
                yearBase.add(QPreMemberInfo.preMemberInfo.year.eq(parseInt(value)));
            }
            whereBase.add(yearBase.stream().reduce(BooleanExpression::or).orElse(null));
        }

        if (params.containsKey("rank")){
            var rankBase = new HashSet<BooleanExpression>();
            for (var value : params.get("rank")) {
                rankBase.add(QPreMemberInfo.preMemberInfo.rank.id.eq(parseLong(value)));
            }
            whereBase.add(rankBase.stream().reduce(BooleanExpression::or).orElse(null));
        }

        //like keys
        if (params.containsKey("name")){
            var value = params.getFirst("name");
            whereBase.add(QPreMemberInfo.preMemberInfo.name.like("%"+value+"%"));
        }

        if (params.containsKey("studentId")){
            var value = params.getFirst("studentId");
            whereBase.add(QPreMemberInfo.preMemberInfo.studentId.like("%"+value+"%"));
        }

        //특수 옵션
        //회원가입 코드가 있는지 없는지
        if (params.containsKey("hasCode")){
            var value = parseBoolean(params.getFirst("hasCode"));

            if(value){
                whereBase.add(QPreMemberInfo.preMemberInfo.registerCode.code.isNotNull());
            }else{
                whereBase.add(QPreMemberInfo.preMemberInfo.registerCode.code.isNull());
            }
        }

        var combineWhere = whereBase.stream().reduce(BooleanExpression::and).orElse(null);

        jpqQuery.where(combineWhere);
    }
}
