package com.jaramgroupware.mms.utlis.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {
    ASC("ASC"),
    DESC("DESC");

    private final String name;
}
