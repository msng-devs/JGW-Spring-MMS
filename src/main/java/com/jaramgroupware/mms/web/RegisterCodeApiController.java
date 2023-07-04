package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.dto.registerCode.RegisterCodeResponseDto;
import com.jaramgroupware.mms.dto.registerCode.RegisterResponseDto;
import com.jaramgroupware.mms.dto.registerCode.controllerDto.RegisterCodeAddRequestControllerDto;
import com.jaramgroupware.mms.service.MemberService;
import com.jaramgroupware.mms.service.RegisterCodeService;
import com.jaramgroupware.mms.utils.aop.routeOption.rbac.RbacOption;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/preMemberInfo")
public class RegisterCodeApiController {

    private final RegisterCodeService registerCodeService;
    private final MemberService memberService;

    @RbacOption(role = 4)
    @PostMapping("{preMemberId}/code")
    public ResponseEntity<RegisterCodeResponseDto> createRegisterCode(
            @RequestHeader("user_pk") String userPk,
            @Valid @RequestBody RegisterCodeAddRequestControllerDto requestDto,
            @PathVariable Long preMemberId) {
        var result = registerCodeService.createRegisterCode(requestDto.toServiceDto(userPk, preMemberId));
        return ResponseEntity.ok(result);
    }

    @RbacOption
    @DeleteMapping("{preMemberId}/code")
    public ResponseEntity<String> deleteRegisterCode(@PathVariable Long preMemberId) {
        registerCodeService.deleteRegisterCode(preMemberId);
        return ResponseEntity.ok("OK");
    }
}
