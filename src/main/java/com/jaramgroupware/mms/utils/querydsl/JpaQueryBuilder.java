//package com.jaramgroupware.mms.utils.querydsl;
//
//import com.jaramgroupware.mms.utils.exception.request.RequestException;
//import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
//import com.jaramgroupware.mms.utils.querydsl.keys.DateRangeKey;
//import com.jaramgroupware.mms.utils.querydsl.keys.DateTimeRangeKey;
//import com.jaramgroupware.mms.utils.querydsl.keys.EqualKey;
//import com.jaramgroupware.mms.utils.querydsl.keys.LikeKey;
//import com.jaramgroupware.mms.utils.spec.SearchCriteria;
//import com.jaramgroupware.mms.utils.spec.SearchOperation;
//import com.querydsl.core.types.dsl.EntityPathBase;
//import com.querydsl.core.types.dsl.StringPath;
//import com.querydsl.jpa.impl.JPAQuery;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.querydsl.core.types.Predicate;
//
//import static com.jaramgroupware.mms.utils.exception.request.RequestErrorCode.QUERY_PARAM_NOT_VALID;
//
//@RequiredArgsConstructor
//@Component
//public class JpaQueryBuilder {
//
//    private final LocalDateTime maxDateTime = LocalDateTime.parse("9999-12-31 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//    private final LocalDateTime minDateTime = LocalDateTime.parse("0001-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//    private final LocalDate maxDate = LocalDate.parse("9999-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    private final LocalDate minDate = LocalDate.parse("0001-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//    private final ParseByNameBuilder parseByNameBuilder;
//
//    public <T extends EntityPathBase<?>> Predicate build(T entityPathBase,
//                                                      MultiValueMap<String, String> params,
//                                                      List<EqualKey> equalKeys,
//                                                      List<LikeKey> likeKeys,
//                                                      List<DateRangeKey> dateRangeKeys,
//                                                      List<DateTimeRangeKey> dateTimeRangeKeys
//    ) {
//        var predicates = new ArrayList<Predicate>();
//
//        if (!equalKeys.isEmpty()) predicates.addAll(buildEqualKey(equalKeys, params));
//        if (!likeKeys.isEmpty()) predicates.addAll(buildLikeKey(likeKeys, params));
//        if (!dateRangeKeys.isEmpty()) predicates.add(buildDateRangeKeys(dateRangeKeys, params));
//        if (!dateTimeRangeKeys.isEmpty()) predicates.add(buildDateTimeRangeKey(dateTimeRangeKeys, params));
//
//        return query;
//    }
//
//
//
//    private <T extends EntityPathBase<?>> List<Predicate> buildEqualKey(EntityPathBase<T> entityPathBase ,List<EqualKey> equalKeys, MultiValueMap<String, String> params) {
//
//        var predicates = new ArrayList<Predicate>();
//
//        for (var key : equalKeys) {
//            if (params.containsKey(key.getQueryParamName())) {
//                var path = entityPathBase.eq(key.getTargetColumn());
//                for (var value : params.get(key.getQueryParamName())) {
//                    predicates.add( );
//                }
//            }
//        }
//        return predicates;
//    }
//
//    private List<Predicate> buildLikeKey(List<LikeKey> likeKeys, MultiValueMap<String, String> params) {
//        var predicates = new ArrayList<Predicate>();
//
//        for (var key : likeKeys) {
//            if (params.containsKey(key.getQueryParamName())) {
//                for (var value : params.get(key.getQueryParamName())) {
//                    predicates.add(
//                            key.getTargetColumn().like("%" + value + "%")
//                    );
//                }
//            }
//        }
//
//        return predicates;
//    }
//    private Predicate buildDateRangeKeys(List<DateRangeKey> dateRangeKeys, MultiValueMap<String, String> params) {
//
//        for (var key: dateRangeKeys) {
//
//            if(params.containsKey(key.getStartQueryParamName()) || params.containsKey(key.getEndQueryParamName()) ){
//                var start = minDate;
//                var end = maxDate;
//                try {
//                    start = (params.containsKey(key.getStartQueryParamName()))
//                            ? LocalDate.parse(params.getFirst(key.getStartQueryParamName()), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                            : minDate;
//                    end = (params.containsKey(key.getEndQueryParamName()))
//                            ? LocalDate.parse(params.getFirst(key.getEndQueryParamName()),DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                            : maxDate;
//                } catch (Exception e){
//                    throw new RequestException(QUERY_PARAM_NOT_VALID,key.getStartQueryParamName()+"혹은"+key.getEndQueryParamName()+"의 형식이 잘못됬습니다.");
//                }
//
//                if(start.isAfter(end)){
//                    throw new RequestException(QUERY_PARAM_NOT_VALID,key.getStartQueryParamName()+"과"+ key.getStartQueryParamName()+"인자가 잘못됬습니다. 범위가 적절한지 다시 확인하세요.");
//                }
//                key.getTargetColumn().between(start.atStartOfDay(),end.atStartOfDay());
//            }
//        }
//
//        return query;
//    }
//
//    private JPAQuery<?> buildDateTimeRangeKey(List<DateTimeRangeKey> dateTimeRangeKeys, MultiValueMap<String, String> params) {
//
//        return query;
//    }
//}
