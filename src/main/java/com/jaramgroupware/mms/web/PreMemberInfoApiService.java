package com.jaramgroupware.mms.web;


import com.jaramgroupware.mms.dto.preMemberInfo.PreMemberInfoResponseDto;
import com.jaramgroupware.mms.dto.preMemberInfo.contollerDto.PreMemberInfoAddRequestControllerDto;
import com.jaramgroupware.mms.dto.preMemberInfo.contollerDto.PreMemberInfoUpdateRequestControllerDto;
import com.jaramgroupware.mms.service.PreMemberInfoService;
import com.jaramgroupware.mms.utils.aop.routeOption.rbac.RbacOption;
import com.jaramgroupware.mms.utils.validation.page.PageableSortKeyCheck;
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
@RequestMapping("/api/v1/preMemberInfo")
public class PreMemberInfoApiService {

    private final PreMemberInfoService preMemberInfoService;


    @RbacOption(role = 4)
    @GetMapping("{preMemberInfoId}")
    public ResponseEntity<PreMemberInfoResponseDto> getPreMemberInfoById(
            @PathVariable Long preMemberInfoId) {

        var result = preMemberInfoService.findById(preMemberInfoId);

        return ResponseEntity.ok(result);
    }

//    @RbacOption(role = 4)
//    @GetMapping
//    public ResponseEntity<List<PreMemberInfoResponseDto>> getPreMemberInfoAll(
//            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC)
//            @PageableSortKeyCheck(sortKeys =
//                    {"id", "name", "role", "studentId", "year", "rank", "major"}
//            ) Pageable pageable,
//            @RequestParam(required = false) MultiValueMap<String, String> queryParam) {
//        var spec = preMemberInfoSpecificationBuilder.toSpec(queryParam);
//        var results = preMemberInfoService.findAll(spec, pageable);
//
//        return ResponseEntity.ok(results);
//    }

    @RbacOption(role = 4)
    @PostMapping
    public ResponseEntity<PreMemberInfoResponseDto> createPreMemberInfo(
            @RequestBody PreMemberInfoAddRequestControllerDto requestDto) {

        var result = preMemberInfoService.createPreMemberInfo(requestDto.toServiceDto());

        return ResponseEntity.ok(result);
    }

    @RbacOption(role = 4)
    @PutMapping("{preMemberInfoId}")
    public ResponseEntity<PreMemberInfoResponseDto> updatePreMemberInfo(
            @PathVariable Long preMemberInfoId,
            @RequestBody PreMemberInfoUpdateRequestControllerDto requestDto) {

        var result = preMemberInfoService.updatePreMemberInfo(requestDto.toServiceDto(preMemberInfoId));

        return ResponseEntity.ok(result);
    }

    @RbacOption(role = 4)
    @DeleteMapping("{preMemberInfoId}")
    public ResponseEntity<String> deletePreMemberInfo(
            @PathVariable Long preMemberInfoId) {

        preMemberInfoService.deletePreMemberInfo(preMemberInfoId);

        return ResponseEntity.ok("OK");
    }

}
