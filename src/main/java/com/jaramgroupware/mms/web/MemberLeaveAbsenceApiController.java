package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.dto.general.controllerDto.MessageDto;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberBulkDeleteRequestControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceAddRequestControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceIdResponseControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceResponseControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceUpdateRequestControllerDto;
import com.jaramgroupware.mms.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/member-leave-absence")
public class MemberLeaveAbsenceApiController {

    private final MemberLeaveAbsenceService memberLeaveAbsenceService;
    private final MemberService memberService;
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

        MemberLeaveAbsenceResponseControllerDto result = memberLeaveAbsenceService.findByMember(memberService.findById(memberId).toEntity()).toControllerDto();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{memberID}")
    public ResponseEntity<MemberLeaveAbsenceIdResponseControllerDto> delMemberLeaveAbsence(
            @PathVariable String memberID,
            @RequestHeader("user_pk") String uid){

        Integer result = memberLeaveAbsenceService.delete(memberLeaveAbsenceService.findByMember(memberService.findById(memberID).toEntity()).getId());

        return ResponseEntity.ok(new MemberLeaveAbsenceIdResponseControllerDto(result));
    }

    @PutMapping("{memberID}")
    public ResponseEntity<MemberLeaveAbsenceResponseControllerDto> updateMemberLeaveAbsence(
            @PathVariable String memberID,
            @RequestBody @Valid MemberLeaveAbsenceUpdateRequestControllerDto memberLeaveAbsenceUpdateRequestControllerDto,
            @RequestHeader("user_pk") String uid) {

        MemberLeaveAbsenceResponseControllerDto result = memberLeaveAbsenceService.update(memberLeaveAbsenceService.findByMember(memberService.findById(memberID).toEntity()).toControllerDto().getId(),memberLeaveAbsenceUpdateRequestControllerDto.toServiceDto()).toControllerDto();

        return ResponseEntity.ok(result);
    }

}

