package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
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
@RequestMapping("/api/v1/registerCode")
public class RegisterCodeApiController {

    private final RegisterCodeService registerCodeService;
    private final MemberService memberService;

    @RbacOption(role=4)
    @PostMapping
    public ResponseEntity<RegisterResponseDto> createRegisterCode(
            @RequestHeader("user_pk") String userPk,
            @Valid @RequestBody RegisterCodeAddRequestControllerDto requestDto
    )
    {
        var result = memberService.registerMemberInfo(requestDto.toServiceDto(userPk));
        return ResponseEntity.ok(result);
    }

    @RbacOption(role=4)
    @PutMapping("{registerCode}")
    public ResponseEntity<RegisterResponseDto> updateRegisterCode(
            @RequestHeader("user_pk") String userPk,
            @PathVariable String registerCode,
            @Valid @RequestBody RegisterCodeAddRequestControllerDto requestDto
    )
    {
        var result = registerCodeService.updateRegisterCode(registerCode,requestDto.toServiceDto(userPk));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{registerCode}")
    public ResponseEntity<String> deleteRegisterCode(@PathVariable String registerCode){
        registerCodeService.deleteRegisterCode(registerCode);
        return ResponseEntity.ok("OK");
    }
}
