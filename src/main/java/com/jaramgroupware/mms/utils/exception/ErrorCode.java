package com.jaramgroupware.mms.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    INVALID_MEMBER_ID(HttpStatus.NOT_FOUND,"INVALID_MEMBER_ID","해당 memberId 을 찾을 수 없습니다!","MM-NF-001"),
    INVALID_MEMBER_INFO(HttpStatus.NOT_FOUND,"INVALID_MEMBER_INFO","해당 memberInfo 을 찾을 수 없습니다!","MM-NF-002"),
    INVALID_MEMBER_LEAVE_ABSENCE_ID(HttpStatus.NOT_FOUND,"INVALID_MEMBER_LEAVE_ABSENCE_ID","해당 memberLeaveAbsenceId 을 찾을 수 없습니다!","MM-NF-003"),
    INVALID_ROLE_ID(HttpStatus.NOT_FOUND,"INVALID_ROLE_ID","해당 roleId 을 찾을 수 없습니다!","MM-NF-004"),
    INVALID_RANK_ID(HttpStatus.NOT_FOUND,"INVALID_ROLE_ID","해당 rankId 을 찾을 수 없습니다!","MM-NF-005"),
    INVALID_MAJOR_ID(HttpStatus.NOT_FOUND,"INVALID_MAJOR_ID","해당 majorId 을 찾을 수 없습니다!","MM-NF-006"),

    EMPTY_MEMBER(HttpStatus.NOT_FOUND,"EMPTY_MEMBER","member가 존재하지 않습니다.","MM-EM-001"),
    EMPTY_MEMBER_INFO(HttpStatus.NOT_FOUND,"EMPTY_MEMBER_INFO","member info가 존재하지 않습니다.","MM-EM-002"),
    EMPTY_MEMBER_LEAVE_ABSENCE(HttpStatus.NOT_FOUND,"EMPTY_MEMBER_LEAVE_ABSENCE","member leave absence가 존재하지 않습니다.","MM-EM-003"),
    EMPTY_ROLE(HttpStatus.NOT_FOUND,"EMPTY_ROLE","role이 존재하지 않습니다.","MM-EM-004"),
    EMPTY_RANK(HttpStatus.NOT_FOUND,"EMPTY_RANK","rank가 존재하지 않습니다.","MM-EM-005"),
    EMPTY_MAJOR(HttpStatus.NOT_FOUND,"EMPTY_MAJOR","major가 존재하지 않습니다.","MM-EM-006"),

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST,"DUPLICATED_EMAIL","중복된 이메일입니다.","MM-DU-001"),
    DUPLICATED_STUDENT_ID(HttpStatus.BAD_REQUEST,"DUPLICATED_STUDENT_ID","중복된 학번입니다.","MM-DU-002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR","알 수 없는 서버 에러입니다.",null);

    private final HttpStatus httpStatus;
    private final String error;
    private final String message;
    private final String errorCode;
}
