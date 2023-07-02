package com.jaramgroupware.mms.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Member의 Status를 나타내는 Enum
 */
@Getter
@AllArgsConstructor
public enum MemberStat {

    ACTIVATED("ACTIVATED"),
    NOT_ACTIVATED("NOT_ACTIVATED"),
    IN_WITHDRAWAL("IN_WITHDRAWAL"),
    NOT_REGISTERED("NOT_REGISTERED");

    private final String status;
}
