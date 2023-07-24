package com.jaramgroupware.mms.domain;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.domain.Sort;

import java.util.List;

public class QSortKey<T extends EntityPathBase<?>> {
    public OrderSpecifier<?> getOrderSpecifier(String property, Sort.Direction direction){
        throw new UnsupportedOperationException("getOrderSpecifier() must be overridden");
    }

    public List<String> getProperties(){
        throw new UnsupportedOperationException("getProperties() must be overridden");
    }
}
