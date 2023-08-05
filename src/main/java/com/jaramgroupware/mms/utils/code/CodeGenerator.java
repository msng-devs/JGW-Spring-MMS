package com.jaramgroupware.mms.utils.code;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CodeGenerator {
    public String generate(){
        return UUID.randomUUID().toString();
    }
}
