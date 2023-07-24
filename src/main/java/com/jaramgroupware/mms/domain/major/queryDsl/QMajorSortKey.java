package com.jaramgroupware.mms.domain.major.queryDsl;

import com.jaramgroupware.mms.domain.QSortKey;
import com.jaramgroupware.mms.domain.major.QMajor;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class QMajorSortKey extends QSortKey<QMajor> {

    private final QMajor major = QMajor.major;

    @Override
    public OrderSpecifier<?> getOrderSpecifier(String property, Sort.Direction direction) {
        return switch (property) {
            case "id" -> (direction.isAscending()) ? major.id.asc() : major.id.desc();
            case "name" -> (direction.isAscending()) ? major.name.asc() : major.name.desc();
            default -> throw new IllegalArgumentException("Invalid property " + property);
        };
    }

    @Override
    public List<String> getProperties() {
        return List.of("id", "name");
    }
}
