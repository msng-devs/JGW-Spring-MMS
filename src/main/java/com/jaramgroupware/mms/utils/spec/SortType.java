package com.jaramgroupware.mms.utils.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {
    ASC("ASC"),
    DESC("DESC");

    private final String name;
}
