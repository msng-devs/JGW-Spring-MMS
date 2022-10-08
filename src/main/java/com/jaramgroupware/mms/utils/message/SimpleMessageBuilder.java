package com.jaramgroupware.mms.utils.message;


import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class SimpleMessageBuilder {

    public Map<String,Object> getString(String arg,Object value) {
        return Collections.singletonMap(arg, value);
    }
}
