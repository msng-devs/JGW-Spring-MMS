package com.jaramgroupware.mms.web;



import com.jaramgroupware.mms.dto.member.MemberResponseDto;
import com.jaramgroupware.mms.dto.member.StatusResponseDto;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberEditRequestControllerDto;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberRegisterRequestControllerDto;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberUpdateRequestControllerDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberEditRequestServiceDto;
import com.jaramgroupware.mms.dto.memberView.MemberViewDatailResponseDto;
import com.jaramgroupware.mms.dto.withdrawal.WithdrawalResponseDto;
import com.jaramgroupware.mms.service.*;
import com.jaramgroupware.mms.utils.aop.routeOption.auth.AuthOption;
import com.jaramgroupware.mms.utils.aop.routeOption.onlyToken.OnlyTokenOption;
import com.jaramgroupware.mms.utils.aop.routeOption.rbac.RbacOption;
import com.jaramgroupware.mms.utils.exception.controller.ControllerErrorCode;
import com.jaramgroupware.mms.utils.exception.controller.ControllerException;
import com.jaramgroupware.mms.utils.validation.page.PageableSortKeyCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberApiController {

    private final MemberService memberService;
    private final MemberViewService memberViewService;



    @OnlyTokenOption
    @PostMapping("/register/{registerCode}")
    public ResponseEntity<?> registerMember(
            @PathVariable String registerCode,
            @RequestBody @Valid MemberRegisterRequestControllerDto dto,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("user_email") String email) {

        var result = memberService.registerMember(dto.toServiceDto(uid, registerCode, email));

        return ResponseEntity.ok(result);
    }


    @AuthOption
    @GetMapping("{memberId}")
    public ResponseEntity<?> getMemberById(
            @PathVariable String memberId,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID,
            @RequestParam(required = false, name = "isDetail") boolean isDetail) {

        if (isDetail) {
            checkCanUseDetailOption(memberId, uid, roleID);
            return ResponseEntity.ok(memberViewService.findByIdDetail(memberId));
        } else {
            return ResponseEntity.ok(memberViewService.findById(memberId));
        }

    }

//    @RbacOption(role = 4)
//    @GetMapping
//    public ResponseEntity<List<MemberViewDatailResponseDto>> getMemberAll(
//            @PageableDefault(page = 0, size = 100, sort = "uid", direction = Sort.Direction.DESC)
//            @PageableSortKeyCheck(sortKeys =
//                    {"uid", "name", "email", "role", "status", "cellPhoneNumber", "studentId", "year", "rank", "major", "dateOfBirth", "isLeaveAbsence"}
//            ) Pageable pageable,
//            @RequestParam(required = false) MultiValueMap<String, String> queryParam) {
//        var spec = memberViewSpecificationBuilder.toSpec(queryParam);
//        var results = memberViewService.findAll(spec, pageable);
//
//        return ResponseEntity.ok(results);
//    }


    @RbacOption(role = 4)
    @DeleteMapping("{memberID}")
    public ResponseEntity<String> deleteMember(@PathVariable String memberID) {
        memberService.deleteById(memberID);
        return ResponseEntity.ok("OK");
    }


    @RbacOption(role = 4)
    @PutMapping("{memberID}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable String memberID,
            @RequestHeader("user_pk") String uid,
            @RequestBody @Valid MemberUpdateRequestControllerDto requestDto) {
        var result = memberService.update(requestDto.toServiceDto(uid, memberID));

        return ResponseEntity.ok(result);
    }

    @AuthOption
    @PostMapping("/withdrawal")
    public ResponseEntity<WithdrawalResponseDto> withdrawalMember(@RequestHeader("user_pk") String uid) {
        var result = memberService.withdrawal(uid);
        return ResponseEntity.ok(result);
    }

    @OnlyTokenOption
    @DeleteMapping("/withdrawal")
    public ResponseEntity<String> cancelWithdrawalMember(@RequestHeader("user_pk") String uid) {
        memberService.cancelWithdrawal(uid);
        return ResponseEntity.ok("OK");
    }

    //자기자신용
    @AuthOption
    @PutMapping("/edit")
    public ResponseEntity<MemberResponseDto> editMember(
            @RequestHeader("user_pk") String uid,
            @RequestBody MemberEditRequestControllerDto requestDto) {

        var result = memberService.edit(requestDto.toServiceDto(uid, uid));
        return ResponseEntity.ok(result);

    }

    @OnlyTokenOption
    @GetMapping("/status")
    public ResponseEntity<StatusResponseDto> getStatus(@RequestHeader("user_pk") String uid) {
        var result = memberService.getStatusById(uid);
        return ResponseEntity.ok(result);
    }

    /**
     * Detail 기능을 사용하기에 적절한 role 과 정보인지 확인하는 함수.
     * 자기 자신이거나, admin이 아닐경우 detail 정보를 받을 수 없다.
     */
    private void checkCanUseDetailOption(String memberId, String uid, Integer roleID) {

        if (!uid.equals(memberId) && roleID < 4) {
            throw new ControllerException(ControllerErrorCode.NOT_AUTHORIZED, "해당 옵션을 사용할 권한이 없습니다.");
        }

    }

}
