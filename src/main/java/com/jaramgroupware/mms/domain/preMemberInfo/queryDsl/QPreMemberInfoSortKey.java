package com.jaramgroupware.mms.domain.preMemberInfo.queryDsl;

import com.jaramgroupware.mms.domain.QSortKey;
import com.jaramgroupware.mms.domain.major.QMajor;
import com.jaramgroupware.mms.domain.memberInfo.QMemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.QPreMemberInfo;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class QPreMemberInfoSortKey extends QSortKey<QMemberInfo> {
    private final QPreMemberInfo preMemberInfo = QPreMemberInfo.preMemberInfo;

    @Override
    public OrderSpecifier<?> getOrderSpecifier(String property, Sort.Direction direction) {
        return switch (property) {
            case "id" -> (direction.isAscending()) ? preMemberInfo.id.asc() : preMemberInfo.id.desc();
            case "studentId" ->
                    (direction.isAscending()) ? preMemberInfo.studentId.asc() : preMemberInfo.studentId.desc();
            case "year" -> (direction.isAscending()) ? preMemberInfo.year.asc() : preMemberInfo.year.desc();
            case "rank" -> (direction.isAscending()) ? preMemberInfo.rank.id.asc() : preMemberInfo.rank.id.desc();
            case "major" -> (direction.isAscending()) ? preMemberInfo.major.id.asc() : preMemberInfo.major.id.desc();
            case "name" -> (direction.isAscending()) ? preMemberInfo.name.asc() : preMemberInfo.name.desc();
            case "expectedDateReturnSchool" ->
                    (direction.isAscending()) ? preMemberInfo.expectedDateReturnSchool.asc() : preMemberInfo.expectedDateReturnSchool.desc();
            case "role" -> (direction.isAscending()) ? preMemberInfo.role.id.asc() : preMemberInfo.role.id.desc();
            case "registerCode" ->
                    (direction.isAscending()) ? preMemberInfo.registerCode.code.asc() : preMemberInfo.registerCode.code.desc();
            default -> throw new IllegalArgumentException("Invalid property " + property);
        };
    }

    @Override
    public List<String> getProperties() {
        return List.of("id", "studentId", "year", "rank", "major", "name", "expectedDateReturnSchool", "role", "registerCode");
    }
}

