package com.jaramgroupware.mms.utils.parse;

import com.jaramgroupware.mms.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParseByNameBuilder {
    public Object parse(String target,ParseByNameType targetClass){
        return switch (targetClass) {
            case INTEGER -> Integer.parseInt(target);
            case MEMBER -> Member.builder().id(target).build();
            case LONG -> Long.parseLong(target);
            case BOOLEAN -> Boolean.parseBoolean(target);
            default -> target;
        };
    }
}
