package com.jaramgroupware.mms.domain.member;

import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
import com.jaramgroupware.mms.utils.spec.SearchCriteria;
import com.jaramgroupware.mms.utils.spec.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberSpecificationBuilder {
    private final ParseByNameBuilder parseByNameBuilder;
    private final LocalDateTime maxDateTime = LocalDateTime.parse("9999-12-31 23:59:59",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final LocalDateTime minDateTime = LocalDateTime.parse("0001-01-01 00:00:00",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    private final LocalDate maxDate = LocalDate.parse("9999-12-31",DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private final LocalDate minDate = LocalDate.parse("0001-01-01",DateTimeFormatter.ofPattern("yyyy-MM-dd"));


    private final Role defaultMemberRole = Role.builder().id(1).build();
    private final Role defaultNewMemberRole = Role.builder().id(0).build();

    private final Rank defaultRank = Rank.builder().id(1).build();
    private final Rank defaultNewRank = Rank.builder().id(2).build();

    @Getter
    @AllArgsConstructor
    private enum EqualKeys {

        MAJOR("majorID","major","Integer"),
        RANK("rankID","rank","Integer"),
        ROLE("roleID","role","Integer"),
        YEAR("year","year","Integer"),
        LEAVEABSENCE("leaveAbsence","leaveAbsence","Boolean"),
        CREATEBY("createBy","createBy","String"),
        MODIFIEDBY("modifiedBy","modifiedBy","String");

        private final String queryParamName;
        private final String tableName;
        private final String type;
    }

    @Getter
    @AllArgsConstructor
    private enum RangeKeys {

        CREATEDDATETIME("startCreatedDateTime","endCreatedDateTime","createdDateTime"),
        MODIFIEDDATETIME("startModifiedDateTime","endModifiedDateTime","modifiedDateTime");

        private final String startQueryParamName;
        private final String endQueryParamName;
        private final String tableName;
    }

    @Getter
    @AllArgsConstructor
    private enum DateRangeKeys {

        DATEOFBIRTH("startDateOfBirth","endDateOfBirth","dateOfBirth");

        private final String startQueryParamName;
        private final String endQueryParamName;
        private final String tableName;
    }

    @Getter
    @AllArgsConstructor
    private enum LikeKeys{

        EMAIL("email","email"),
        NAME("name","name"),
        PHONENUMBER("phoneNumber","phoneNumber"),
        STUDENTID("studentID","studentID");

        private final String queryParamName;
        private final String tableName;
    }
    public MemberSpecification toSpec(MultiValueMap<String, String> queryParam){


        MemberSpecification specification = new MemberSpecification();
        boolean isIncludeGuest = Boolean.parseBoolean(queryParam.getFirst("includeGuest"));

        if(!isIncludeGuest){
            specification.add(
                    new SearchCriteria("role",
                            Arrays.asList(defaultMemberRole,defaultNewMemberRole),
                            SearchOperation.NOT_IN)
            );
        }else{
            specification.add(
                    new SearchCriteria("role",
                            Arrays.asList(defaultMemberRole,defaultNewMemberRole),
                            SearchOperation.IN)
            );
        }
        
            for (EqualKeys key: EqualKeys.values()) {
                if(queryParam.containsKey(key.getQueryParamName())){
                    specification.add(new SearchCriteria(key.getTableName()
                            , Collections.singletonList(parseByNameBuilder.parse(queryParam.getFirst(key.getQueryParamName()), key.getType()))
                            ,SearchOperation.EQUAL));
                }
            }

            for (RangeKeys key: RangeKeys.values()) {
                if(queryParam.containsKey(key.getStartQueryParamName()) || queryParam.containsKey(key.getEndQueryParamName()) ){
                    LocalDateTime start = minDateTime;
                    LocalDateTime end = maxDateTime;
                    try {
                        start = (queryParam.containsKey(key.getStartQueryParamName()))
                                ? LocalDateTime.parse(queryParam.getFirst(key.getStartQueryParamName()), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                : minDateTime;
                        end = (queryParam.containsKey(key.getEndQueryParamName()))
                                ? LocalDateTime.parse(queryParam.getFirst(key.getEndQueryParamName()),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                : maxDateTime;
                    }catch (Exception e){
                        throw new IllegalArgumentException(key.getStartQueryParamName()+"혹은"+key.getEndQueryParamName()+"의 형식이 잘못됬습니다.");
                    }

                    if(start.isAfter(end)){
                        throw new IllegalArgumentException(key.getStartQueryParamName()+"과"+ key.getStartQueryParamName()+"인자가 잘못됬습니다. 범위가 적절한지 다시 확인하세요.");
                    }
                    specification.add(new SearchCriteria(key.getTableName(),
                            Arrays.asList(new LocalDateTime[] {start,end}),
                            SearchOperation.BETWEEN
                            ));
                }
            }

        for (DateRangeKeys key: DateRangeKeys.values()) {
            if(queryParam.containsKey(key.getStartQueryParamName()) || queryParam.containsKey(key.getEndQueryParamName()) ){
                LocalDate start = minDate;
                LocalDate end = maxDate;
                try {
                    start = (queryParam.containsKey(key.getStartQueryParamName()))
                            ? LocalDate.parse(queryParam.getFirst(key.getStartQueryParamName()), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            : minDate;
                    end = (queryParam.containsKey(key.getEndQueryParamName()))
                            ? LocalDate.parse(queryParam.getFirst(key.getEndQueryParamName()),DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            : maxDate;
                }catch (Exception e){
                    throw new IllegalArgumentException(key.getStartQueryParamName()+"혹은"+key.getEndQueryParamName()+"의 형식이 잘못됬습니다.");
                }

                if(start.isAfter(end)){
                    throw new IllegalArgumentException(key.getStartQueryParamName()+"과"+ key.getStartQueryParamName()+"인자가 잘못됬습니다. 범위가 적절한지 다시 확인하세요.");
                }
                specification.add(new SearchCriteria(key.getTableName(),
                        Arrays.asList(new LocalDate[] {start,end}),
                        SearchOperation.BETWEEN_DATE
                ));
            }
        }
            for (LikeKeys key : LikeKeys.values()) {
                if(queryParam.containsKey(key.getQueryParamName())){
                    specification.add(new SearchCriteria(key.getTableName()
                            , Arrays.asList(new String[]{queryParam.getFirst(key.getQueryParamName())})
                            ,SearchOperation.MATCH));
                }
            }
            return specification;
    }

}

