package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.dto.memberLeaveAbsence.MemberLeaveAbsenceResponseDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceUpdateRequestControllerDto;
import com.jaramgroupware.mms.service.LeaveAbsenceService;
import com.jaramgroupware.mms.utils.aop.routeOption.auth.AuthOption;
import com.jaramgroupware.mms.utils.exception.controller.ControllerErrorCode;
import com.jaramgroupware.mms.utils.exception.controller.ControllerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class LeaveAbsenceApiController {

    private final LeaveAbsenceService leaveAbsenceService;

    @AuthOption
    @GetMapping("/{memberId}/leave-absence")
    public ResponseEntity<MemberLeaveAbsenceResponseDto> findLeaveAbsenceByMemberId(
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Long role,
            @PathVariable String memberId
    ) {
        checkIsCanUse(memberId, uid, role);
        var result = leaveAbsenceService.findByMember(memberId);
        return ResponseEntity.ok(result);
    }

    @AuthOption
    @PutMapping("/{memberId}/leave-absence")
    public ResponseEntity<MemberLeaveAbsenceResponseDto> updateLeaveAbsenceByMemberId(
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Long role,
            @PathVariable String memberId,
            @RequestBody MemberLeaveAbsenceUpdateRequestControllerDto dto
    ) {
        checkIsCanUse(memberId, uid, role);
        var result = leaveAbsenceService.update(dto.toServiceDto(memberId));
        return ResponseEntity.ok(result);
    }

    private void checkIsCanUse(String targetMemberId, String uid, Long roleID) {
        if (!targetMemberId.equals(uid) && roleID < 4L) {
            throw new ControllerException(ControllerErrorCode.NOT_AUTHORIZED, "해당 API를 사용할 권한이 없습니다.");
        }
    }
}
