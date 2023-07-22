//package com.jaramgroupware.mms.utils.spec;
//
//import com.jaramgroupware.mms.utils.exception.request.RequestException;
//import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
//import com.jaramgroupware.mms.utils.querydsl.keys.DateRangeKey;
//import com.jaramgroupware.mms.utils.querydsl.keys.DateTimeRangeKey;
//import com.jaramgroupware.mms.utils.querydsl.keys.EqualKey;
//import com.jaramgroupware.mms.utils.querydsl.keys.LikeKey;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static com.jaramgroupware.mms.utils.exception.request.RequestErrorCode.*;
//
//@RequiredArgsConstructor
//@Component
//public class SpecificationBuilder {
//    private final LocalDateTime maxDateTime = LocalDateTime.parse("9999-12-31 23:59:59",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//    private final LocalDateTime minDateTime = LocalDateTime.parse("0001-01-01 00:00:00",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//    private final LocalDate maxDate = LocalDate.parse("9999-12-31",DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    private final LocalDate minDate = LocalDate.parse("0001-01-01",DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//    private final ParseByNameBuilder parseByNameBuilder;
//    public List<SearchCriteria> buildSearchCriteria(
//            List<DateRangeKey> dateRangeKeys,
//            List<EqualKey> equalKeys,
//            List<LikeKey> likeKeys,
//            List<DateTimeRangeKey> dateTimeRangeKeys,
//            MultiValueMap<String, String> queryParam
//    ){
//        List<SearchCriteria> specification = Collections.emptyList();
//
//        if (!equalKeys.isEmpty()) parseEqualKeys(equalKeys, queryParam,specification);
//
//        if (!dateRangeKeys.isEmpty()) parseDateRangeKeys(dateRangeKeys, queryParam,specification);
//
//        if (!dateTimeRangeKeys.isEmpty()) parseDateTimeRangeKeys(dateTimeRangeKeys, queryParam,specification);
//
//        if (!likeKeys.isEmpty()) parseLikeKeys(likeKeys, queryParam,specification);
//
//        return specification;
//    }
//
//    private void parseEqualKeys(List<EqualKey> equalKeys, MultiValueMap<String, String> queryParam,List<SearchCriteria> specification){
//        for (EqualKey key : equalKeys) {
//            if(queryParam.containsKey(key.getQueryParamName())){
//                specification.add(new SearchCriteria(key.getTableName()
//                        , Collections.singletonList(parseByNameBuilder.parse(queryParam.getFirst(key.getQueryParamName()), key.getType()))
//                        ,SearchOperation.EQUAL));
//            }
//        }
//    }
//
//    private void parseDateRangeKeys(List<DateRangeKey> dateRangeKeys, MultiValueMap<String, String> queryParam,List<SearchCriteria> specification){
//        for (DateRangeKey key: dateRangeKeys) {
//            if(queryParam.containsKey(key.getStartQueryParamName()) || queryParam.containsKey(key.getEndQueryParamName()) ){
//                LocalDate start = minDate;
//                LocalDate end = maxDate;
//                try {
//                    start = (queryParam.containsKey(key.getStartQueryParamName()))
//                            ? LocalDate.parse(queryParam.getFirst(key.getStartQueryParamName()), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                            : minDate;
//                    end = (queryParam.containsKey(key.getEndQueryParamName()))
//                            ? LocalDate.parse(queryParam.getFirst(key.getEndQueryParamName()),DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                            : maxDate;
//                } catch (Exception e){
//                    throw new RequestException(QUERY_PARAM_NOT_VALID,key.getStartQueryParamName()+"혹은"+key.getEndQueryParamName()+"의 형식이 잘못됬습니다.");
//                }
//
//                if(start.isAfter(end)){
//                    throw new RequestException(QUERY_PARAM_NOT_VALID,key.getStartQueryParamName()+"과"+ key.getStartQueryParamName()+"인자가 잘못됬습니다. 범위가 적절한지 다시 확인하세요.");
//                }
//                specification.add(new SearchCriteria(key.getTableName(),
//                        Arrays.asList(new LocalDate[] {start,end}),
//                        SearchOperation.BETWEEN_DATE
//                ));
//            }
//        }
//    }
//
//    private void parseDateTimeRangeKeys(List<DateTimeRangeKey> dateTimeRangeKeys, MultiValueMap<String, String> queryParam,List<SearchCriteria> specification){
//        for (DateTimeRangeKey key: dateTimeRangeKeys) {
//            if(queryParam.containsKey(key.getStartQueryParamName()) || queryParam.containsKey(key.getEndQueryParamName()) ){
//                LocalDateTime start = minDateTime;
//                LocalDateTime end = maxDateTime;
//                try {
//                    start = (queryParam.containsKey(key.getStartQueryParamName()))
//                            ? LocalDateTime.parse(queryParam.getFirst(key.getStartQueryParamName()), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                            : minDateTime;
//                    end = (queryParam.containsKey(key.getEndQueryParamName()))
//                            ? LocalDateTime.parse(queryParam.getFirst(key.getEndQueryParamName()),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                            : maxDateTime;
//                }catch (Exception e){
//                    throw new RequestException(QUERY_PARAM_NOT_VALID,key.getStartQueryParamName()+"혹은"+key.getEndQueryParamName()+"의 형식이 잘못됬습니다.");
//                }
//
//                if(start.isAfter(end)){
//                    throw new RequestException(QUERY_PARAM_NOT_VALID,key.getStartQueryParamName()+"과"+ key.getStartQueryParamName()+"인자가 잘못됬습니다. 범위가 적절한지 다시 확인하세요.");
//                }
//                specification.add(new SearchCriteria(key.getTableName(),
//                        Arrays.asList(new LocalDateTime[] {start,end}),
//                        SearchOperation.BETWEEN
//                ));
//            }
//        }
//    }
//
//    private void parseLikeKeys(List<LikeKey> likeKeys, MultiValueMap<String, String> queryParam,List<SearchCriteria> specification){
//        for (LikeKey key : likeKeys) {
//            if(queryParam.containsKey(key.getQueryParamName())){
//                specification.add(new SearchCriteria(key.getTableName()
//                        , Arrays.asList(new String[]{queryParam.getFirst(key.getQueryParamName())})
//                        ,SearchOperation.MATCH));
//            }
//        }
//    }
//
//}
