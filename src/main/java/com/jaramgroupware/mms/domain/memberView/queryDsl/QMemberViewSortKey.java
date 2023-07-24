package com.jaramgroupware.mms.domain.memberView.queryDsl;

import com.jaramgroupware.mms.domain.QSortKey;
import com.jaramgroupware.mms.domain.memberView.QMemberView;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class QMemberViewSortKey extends QSortKey<QMemberView> {

    private final QMemberView qMemberView = QMemberView.memberView;

    @Override
    public OrderSpecifier<?> getOrderSpecifier(String property, Sort.Direction direction){
        return switch (property) {
            case "uid" -> (direction.isAscending()) ? qMemberView.uid.asc() : qMemberView.uid.desc();
            case "name" -> (direction.isAscending()) ? qMemberView.name.asc() : qMemberView.name.desc();
            case "major" -> (direction.isAscending()) ? qMemberView.major.asc() : qMemberView.major.desc();
            case "studentId" -> (direction.isAscending()) ? qMemberView.studentId.asc() : qMemberView.studentId.desc();
            case "email" -> (direction.isAscending()) ? qMemberView.email.asc() : qMemberView.email.desc();
            case "cellPhoneNumber" -> (direction.isAscending()) ? qMemberView.cellPhoneNumber.asc() : qMemberView.cellPhoneNumber.desc();
            case "role" -> (direction.isAscending()) ? qMemberView.role.asc() : qMemberView.role.desc();
            case "status" -> (direction.isAscending()) ? qMemberView.status.asc() : qMemberView.status.desc();
            case "dateOfBirth" -> (direction.isAscending()) ? qMemberView.dateOfBirth.asc() : qMemberView.dateOfBirth.desc();
            case "year" -> (direction.isAscending()) ? qMemberView.year.asc() : qMemberView.year.desc();
            case "rank" -> (direction.isAscending()) ? qMemberView.rank.asc() : qMemberView.rank.desc();
            case "isLeaveAbsence" -> (direction.isAscending()) ? qMemberView.isLeaveAbsence.asc() : qMemberView.isLeaveAbsence.desc();
            default -> throw new IllegalArgumentException("Invalid property " + property);
        };
    }

    @Override
    public List<String> getProperties(){
        return List.of("uid", "name", "major", "studentId", "email", "cellPhoneNumber", "role", "status", "dateOfBirth", "year", "rank", "isLeaveAbsence");
    }
}
