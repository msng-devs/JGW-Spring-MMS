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

/**
 * MemberLeaveAbsence Api Controller 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/member-leave-absence")
public class MemberLeaveAbsenceApiController {

    private final MemberLeaveAbsenceService memberLeaveAbsenceService;
    private final MemberService memberService;
    private final MemberLeaveAbsenceSpecificationBuilder memberLeaveAbsenceSpecificationBuilder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 단일 회원 휴학 정보를 등록하는 함수
     * @param memberLeaveAbsenceAddRequestControllerDto MemberLeaveAbsence(Object)의 등록 요청 정보를 담고 있는 dto
     * @param uid 해당 등록을 요청한 Member(Object)의 UID(Firebase uid)
     * @return 등록된 MemberLeaveAbsence(Object)의 ID를 반환
     */
    @PostMapping
    public ResponseEntity<MemberLeaveAbsenceIdResponseControllerDto> addMemberLeaveAbsence(
            @RequestBody @Valid MemberLeaveAbsenceAddRequestControllerDto memberLeaveAbsenceAddRequestControllerDto,
            @RequestHeader("user_pk") String uid) {

        Integer id = memberLeaveAbsenceService.add(memberLeaveAbsenceAddRequestControllerDto.toServiceDto());

        return ResponseEntity.ok(new MemberLeaveAbsenceIdResponseControllerDto(id));
    }

    /**
     * 단일 회원 휴학 정보를 조회하는 함수
     * @param memberId 조회할 회원 휴학 정보의 해당 Member(Object) UID(Firebase uid)
     * @param uid 해당 조회를 요청한 Member(Object)의 UID(Firebase uid)
     * @param roleID 해당 조회를 요청한 Member(Object)의 Role(Object) ID
     * @return 성공적으로 조회 완료 시 해당 MemberLeaveAbsence(Object)의 정보를 담은 dto 반환
     */
    @GetMapping("{memberId}")
    public ResponseEntity<MemberLeaveAbsenceResponseControllerDto> getMemberLeaveAbsenceById(
            @PathVariable String memberId,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID) {

        Member targetMember = memberService.findById(memberId).toEntity();
        MemberLeaveAbsenceResponseControllerDto result = memberLeaveAbsenceService.findByMember(targetMember).toControllerDto();

        return ResponseEntity.ok(result);
    }

    /**
     * 다수 회원 휴학 정보를 조회하는 함수
     * @param pageable sort option
     * @param queryParam query option
     * @param includeGuest 신규 학회원(true), 기존 학회원(false) option
     * @param uid 해당 조회 요청한 Member(Object)의 UID(Firebase uid)
     * @return 다수 MemberLeaveAbsence(Object)의 정보를 담은 dto 반환(List type)
     */
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

    /**
     * 단일 회원 휴학 정보를 삭제하는 함수
     * @param memberID 삭제할 회원 휴학 정보의 해당 Member(Object) UID(Firebase uid)
     * @param uid 해당 삭제 요청한 Member(Object)의 UID(Firebase uid)
     * @return 삭제된 MemberLeaveAbsence(Object)의 ID
     */
    @DeleteMapping("{memberID}")
    public ResponseEntity<MemberLeaveAbsenceIdResponseControllerDto> delMemberLeaveAbsence(
            @PathVariable String memberID,
            @RequestHeader("user_pk") String uid){

        Member targetMember = memberService.findById(memberID).toEntity();
        Integer result = memberLeaveAbsenceService.delete(targetMember);

        return ResponseEntity.ok(new MemberLeaveAbsenceIdResponseControllerDto(result));
    }

    /**
     * 다수 회원 휴학 정보를 삭제하는 함수
     * @param dto 삭제할 다수 MemberLeaveAbsence(Object)의 id들을 담은 dto
     * @param uid 해당 삭제 요청한 Member(Object)의 UID(Firebase uid)
     * @param roleID 해당 삭제 요청한 Member(Object)의 UID(Firebase uid)
     * @return 삭제된 회원 휴학 정보의 개수를 포함한 메시지 반환
     */
    @DeleteMapping
    public ResponseEntity<MessageDto> bulkDelMemberLeaveAbsence(
            @RequestBody @Valid MemberLeaveAbsenceBulkDeleteRequestControllerDto dto,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID){

        memberLeaveAbsenceService.delete(dto.getMemberLeaveAbsenceIDs());

        return ResponseEntity.ok(new MessageDto("총 ("+dto.getMemberLeaveAbsenceIDs().size()+")개의 MemberLeaveAbsence를 성공적으로 삭제했습니다!"));
    }

    /**
     * 단일 회원 휴학 정보를 수정하는 함수
     * @param memberID 수정할 회원 휴학 정보의 해당 Member(Object) UID(Firebase uid)
     * @param memberLeaveAbsenceUpdateRequestControllerDto 수정할 회원 휴학 정보의 수정 내용을 담은 dto
     * @param uid 해당 수정 요청한 Member(Object)의 UID(Firebase uid)
     * @return 수정된 MemberLeaveAbsence(Object)의 정보를 담은 dto 반환
     */
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

