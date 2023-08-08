package com.jaramgroupware.mms.utlis.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;

public class SnakeCaseFieldNamingStrategy implements FieldNamingStrategy {
    @Override
    public String translateName(java.lang.reflect.Field field) {
        return FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field);
    }
}
