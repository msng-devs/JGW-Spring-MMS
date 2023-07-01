package com.jaramgroupware.mms.utils.parse;

import com.jaramgroupware.mms.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParseByNameBuilder {
    public Object parse(String target,String targetClass){
        return switch (targetClass) {
            case "Integer" -> Integer.parseInt(target);
            case "Member" -> Member.builder().id(target).build();
            case "Long" -> Long.parseLong(target);
            case "Bool", "Boolean" -> Boolean.parseBoolean(target);
            default -> target;
        };
    }
}
