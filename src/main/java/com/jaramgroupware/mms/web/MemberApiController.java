package com.jaramgroupware.mms.web;



import com.jaramgroupware.mms.domain.memberView.MemberViewSpecificationBuilder;
import com.jaramgroupware.mms.dto.memberView.MemberViewDatailResponseDto;
import com.jaramgroupware.mms.dto.withdrawal.WithdrawalResponseDto;
import com.jaramgroupware.mms.service.*;
import com.jaramgroupware.mms.utils.aop.routeOption.auth.AuthOption;
import com.jaramgroupware.mms.utils.aop.routeOption.rbac.RbacOption;
import com.jaramgroupware.mms.utils.exception.controller.ControllerErrorCode;
import com.jaramgroupware.mms.utils.exception.controller.ControllerException;
import com.jaramgroupware.mms.utils.validation.page.PageableSortKeyCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Member Api Controller 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberApiController {

    private final MemberService memberService;
    private final MemberViewService memberViewService;
    private final MemberViewSpecificationBuilder memberViewSpecificationBuilder;


    @AuthOption
    @GetMapping("{memberId}")
    public ResponseEntity<?> getMemberById(
            @PathVariable String memberId,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID,
            @RequestParam(required = false,name = "isDetail") boolean isDetail){

        if(isDetail){
            checkCanUseDetailOption(memberId,uid,roleID);
            return ResponseEntity.ok(memberViewService.findByIdDetail(memberId));
        } else {
            return ResponseEntity.ok(memberViewService.findById(memberId));
        }

    }

    /**
     * Detail 기능을 사용하기에 적절한 role 과 정보인지 확인하는 함수.
     * 자기 자신이거나, admin이 아닐경우 detail 정보를 받을 수 없다.
     */
    private void checkCanUseDetailOption(String memberId, String uid, Integer roleID){

        if(!uid.equals(memberId) || roleID < 4 ){
            throw new ControllerException(ControllerErrorCode.NOT_AUTHORIZED,"해당 옵션을 사용할 권한이 없습니다.");
        }

    }

    @RbacOption(role = 4)
    @GetMapping
    public ResponseEntity<List<MemberViewDatailResponseDto>> getMemberAll(
            @PageableDefault(page = 0,size = 100,sort = "uid",direction = Sort.Direction.DESC)
            @PageableSortKeyCheck(sortKeys =
                    {"uid","name","email","role","status","cellPhoneNumber","studentId","year","rank","major","dateOfBirth","isLeaveAbsence"}
            ) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> queryParam)
    {
        var spec = memberViewSpecificationBuilder.toSpec(queryParam);
        var results = memberViewService.findAll(spec,pageable);

        return ResponseEntity.ok(results);
    }

    //어드민용
    @RbacOption(role = 4)
    @DeleteMapping("{memberID}")
    public ResponseEntity<String> deleteMember(@PathVariable String memberID){
        memberService.deleteById(memberID);
        return ResponseEntity.ok("OK");
    }

    //어드민용
    @RbacOption(role = 4)
    @PutMapping("{memberID}")
    public ResponseEntity<MemberViewDatailResponseDto> updateMember(
            @PathVariable String memberID,
            @RequestBody MemberViewDatailResponseDto memberViewDatailResponseDto){

    }

    //자기자신용
    @AuthOption
    @DeleteMapping("/withdrawal")
    public ResponseEntity<WithdrawalResponseDto> withdrawalMember(@RequestHeader("user_pk") String uid){
        var result = memberService.withdraw(uid);
        return ResponseEntity.ok(result);
    }

    //자기자신용
    @AuthOption
    @RbacOption(role = 4)
    @PutMapping("/edit")
    public ResponseEntity<MemberViewDatailResponseDto> editMember(
            @RequestHeader("user_pk") String uid,
            @RequestBody MemberViewDatailResponseDto memberViewDatailResponseDto){

    }

}
