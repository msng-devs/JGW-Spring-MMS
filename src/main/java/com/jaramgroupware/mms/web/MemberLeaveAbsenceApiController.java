package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberSpecificationBuilder;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoSpecification;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceSpecification;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceSpecificationBuilder;
import com.jaramgroupware.mms.dto.general.controllerDto.MessageDto;
import com.jaramgroupware.mms.dto.memberInfo.controllerDto.MemberInfoFullResponseControllerDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoResponseServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.*;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceResponseServiceDto;
import com.jaramgroupware.mms.service.*;
import com.jaramgroupware.mms.utils.validation.PageableValid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/member-leave-absence")
public class MemberLeaveAbsenceApiController {

    private final MemberLeaveAbsenceService memberLeaveAbsenceService;
    private final MemberService memberService;
    private final MemberLeaveAbsenceSpecificationBuilder memberLeaveAbsenceSpecificationBuilder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<MemberLeaveAbsenceIdResponseControllerDto> addMemberLeaveAbsence(
            @RequestBody @Valid MemberLeaveAbsenceAddRequestControllerDto memberLeaveAbsenceAddRequestControllerDto,
            @RequestHeader("user_pk") String uid) {

        Integer id = memberLeaveAbsenceService.add(memberLeaveAbsenceAddRequestControllerDto.toServiceDto());

        return ResponseEntity.ok(new MemberLeaveAbsenceIdResponseControllerDto(id));
    }

    @GetMapping("{memberId}")
    public ResponseEntity<MemberLeaveAbsenceResponseControllerDto> getMemberLeaveAbsenceById(
            @PathVariable String memberId,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID) {

        Member targetMember = memberService.findById(memberId).toEntity();
        MemberLeaveAbsenceResponseControllerDto result = memberLeaveAbsenceService.findByMember(targetMember).toControllerDto();

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<MemberLeaveAbsenceResponseControllerDto>> getMemberLeaveAbsenceAll(
            @PageableDefault(page = 0,size = 1000,sort = "id",direction = Sort.Direction.DESC)
            @PageableValid(sortKeys =
                    {"id","status","expectedDateReturnSchool"}
            ) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> queryParam,
            @RequestParam(required = false,defaultValue = "false") Boolean includeGuest,
            @RequestHeader("user_pk") String uid){

        logger.debug("includeGuest {}", includeGuest);

        //limit 확인 및 추가
        int limit = queryParam.containsKey("limit") ? Integer.parseInt(Objects.requireNonNull(queryParam.getFirst("limit"))) : -1;

        //Specification 등록
        MemberLeaveAbsenceSpecification spec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        List<MemberLeaveAbsenceResponseControllerDto> results;

        //limit true
        if(limit > 0){
            results = memberLeaveAbsenceService.findAll(spec, PageRequest.of(0, limit, pageable.getSort()))
                    .stream()
                    .map(MemberLeaveAbsenceResponseServiceDto::toControllerDto)
                    .collect(Collectors.toList());
        }

        else{
            results = memberLeaveAbsenceService.findAll(spec,pageable)
                    .stream()
                    .map(MemberLeaveAbsenceResponseServiceDto::toControllerDto)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(results);
    }

    @DeleteMapping("{memberID}")
    public ResponseEntity<MemberLeaveAbsenceIdResponseControllerDto> delMemberLeaveAbsence(
            @PathVariable String memberID,
            @RequestHeader("user_pk") String uid){

        Member targetMember = memberService.findById(memberID).toEntity();
        Integer result = memberLeaveAbsenceService.delete(targetMember);

        return ResponseEntity.ok(new MemberLeaveAbsenceIdResponseControllerDto(result));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> bulkDelMemberLeaveAbsence(
            @RequestBody @Valid MemberLeaveAbsenceBulkDeleteRequestControllerDto dto,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID){

        memberLeaveAbsenceService.delete(dto.getMemberLeaveAbsenceIDs());

        return ResponseEntity.ok(new MessageDto("총 ("+dto.getMemberLeaveAbsenceIDs().size()+")개의 MemberLeaveAbsence를 성공적으로 삭제했습니다!"));
    }

    @PutMapping("{memberID}")
    public ResponseEntity<MemberLeaveAbsenceResponseControllerDto> updateMemberLeaveAbsence(
            @PathVariable String memberID,
            @RequestBody @Valid MemberLeaveAbsenceUpdateRequestControllerDto memberLeaveAbsenceUpdateRequestControllerDto,
            @RequestHeader("user_pk") String uid) {

        Member targetMember = memberService.findById(memberID).toEntity();
        MemberLeaveAbsenceResponseControllerDto result = memberLeaveAbsenceService.update(targetMember,memberLeaveAbsenceUpdateRequestControllerDto.toServiceDto()).toControllerDto();

        return ResponseEntity.ok(result);
    }

}

