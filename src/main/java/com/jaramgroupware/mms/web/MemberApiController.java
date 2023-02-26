package com.jaramgroupware.mms.web;


import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberSpecification;
import com.jaramgroupware.mms.domain.member.MemberSpecificationBuilder;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoSpecification;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoSpecificationBuilder;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.general.controllerDto.MessageDto;
import com.jaramgroupware.mms.dto.member.controllerDto.*;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberBulkUpdateRequestControllerDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberResponseServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.controllerDto.MemberInfoFullResponseControllerDto;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberUpdateRequestControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceResponseControllerDto;
import com.jaramgroupware.mms.service.*;
import com.jaramgroupware.mms.utils.validation.PageableValid;
import com.jaramgroupware.mms.utils.validation.member.BulkAddMemberValid;
import com.jaramgroupware.mms.utils.validation.member.BulkUpdateMemberValid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/member")
public class MemberApiController {

    private final MemberService memberService;
    private final MemberInfoService memberInfoService;
    private final MemberLeaveAbsenceService memberLeaveAbsenceService;
    private final MajorService majorService;
    private final RankService rankService;
    private final RoleService roleService;
    private final ConfigService configService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MemberSpecificationBuilder memberSpecificationBuilder;
    private final MemberInfoSpecificationBuilder memberInfoSpecificationBuilder;

    private final Role defaultMemberRole = Role.builder().id(1).build();
    private final Role defaultNewMemberRole = Role.builder().id(0).build();

    private final Rank defaultRank = Rank.builder().id(2).build();
    private final Rank defaultNewRank = Rank.builder().id(1).build();

    private final Role adminRole = Role.builder().id(4).build();


//    @PostMapping("/register")
//    public ResponseEntity<MemberIdResponseControllerDto> registerMember(
//            @RequestParam(defaultValue = "true",name = "isNew") Boolean isNew,
//            @RequestBody @Valid MemberRegisterRequestControllerDto memberRegisterRequestControllerDto,
//            @RequestHeader("user_pk") String uid){
//
//        String id;
//        if(isNew){
//            id = memberService.add(memberRegisterRequestControllerDto.toServiceDto(defaultNewRank,defaultNewMemberRole),"system");
//        } else{
//            id = memberService.add(memberRegisterRequestControllerDto.toServiceDto(defaultRank,defaultMemberRole),"system");
//        }
//
//        return ResponseEntity.ok(new MemberIdResponseControllerDto(id));
//    }

    // 다수 Member를 등록
    @PostMapping
    public ResponseEntity<MessageDto> addMember(
            @RequestBody @NotNull @BulkAddMemberValid Set<@Valid MemberAddRequestControllerDto> memberAddRequestControllerDto,
            @RequestHeader("user_pk") String uid){

        memberService.addAll(
                memberAddRequestControllerDto.stream()
                        .map(MemberAddRequestControllerDto::toMemberAddServiceDto)
                        .collect(Collectors.toList()));

        memberInfoService.addAll(
                memberAddRequestControllerDto.stream()
                        .map(MemberAddRequestControllerDto::toMemberInfoAddServiceDto)
                        .collect(Collectors.toList()), uid);

        return ResponseEntity.ok(new MessageDto("총 ("+memberAddRequestControllerDto.size()+")개의 Member를 성공적으로 추가했습니다!"));
    }

    // 단일 Member를 조회 (Member 정보만 반환)
    // role 조회하여 admin 권한 이상이면 전체 정보를, 아닐 경우 일부 정보(이름 Email 계정활성정보(status))
    @GetMapping("{memberId}")
    public ResponseEntity<?> getMemberById(
            @PathVariable String memberId,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID){

        MemberFullResponseControllerDto result = memberService.findById(memberId).toController();

        if(roleID >= adminRole.getId() || uid.equals(memberId)) return ResponseEntity.ok(result);

        return ResponseEntity.ok(result.toTiny());
    }

    // 단일 Member를 조회 (Member+MemberInfo 정보 반환)
    // role 조회하여 admin 권한 이상이면 전체 정보를, 아닐 경우 일부 정보(이름 학과 학번(일부) 기수 Email)
    @GetMapping("/info/{memberId}")
    public ResponseEntity<?> getMemberInfoById(
            @PathVariable String memberId,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID){

        MemberInfoFullResponseControllerDto result = memberInfoService.findByMember(memberService.findById(memberId).toEntity()).toControllerDto();
        result.setStatus(memberService.findById(memberId).toEntity().isStatus());

        if(roleID >= adminRole.getId() || uid.equals(memberId)) return ResponseEntity.ok(result);

        return ResponseEntity.ok(result.toTiny());
    }

//    //다수 멤버를 조회 (Member+MemberInfo 정보 반환)
//    @GetMapping
//    public ResponseEntity<List<MemberInfoFullResponseControllerDto>> getMemberAll(
//            @PageableDefault(page = 0,size = 1000,sort = "id",direction = Sort.Direction.DESC)
//            @PageableValid(sortKeys =
//                    {"id","email","name","phoneNumber","studentID","major","rank","role","year","leaveAbsence","dateOfBirth","createdDateTime","modifiedDateTime","createBy","modifiedBy"}
//            ) Pageable pageable,
//            @RequestParam(required = false) MultiValueMap<String, String> queryParam,
//            @RequestParam(required = false,defaultValue = "false") Boolean includeGuest,
//            @RequestHeader("user_pk") String uid){
//
//        logger.debug("includeGuest {}", includeGuest);
//
//        //limit 확인 및 추가
//        int limit = queryParam.containsKey("limit") ? Integer.parseInt(Objects.requireNonNull(queryParam.getFirst("limit"))) : -1;
//
//        //Specification 등록
//        MemberSpecification spec = memberSpecificationBuilder.toSpec(queryParam);
//        MemberInfoSpecification spec2 = memberInfoSpecificationBuilder.toSpec(queryParam);
//
//        List<MemberFullResponseControllerDto> results;
//
//        //limit true
//        if(limit > 0){
//            results = memberInfoService.findAll(spec, PageRequest.of(0, limit, pageable.getSort()))
//                    .stream()
//                    .map(MemberResponseServiceDto::toController)
//                    .collect(Collectors.toList());
//        }
//
//        else{
//            results = memberInfoService.findAll(spec,pageable)
//                    .stream()
//                    .map(MemberResponseServiceDto::toController)
//                    .collect(Collectors.toList());
//        }
//
//        return ResponseEntity.ok(results);
//    }


    //단일 멤버 삭제
    @DeleteMapping("{memberID}")
    public ResponseEntity<MemberIdResponseControllerDto> delMember(
            @PathVariable String memberID,
            @RequestHeader("user_pk") String uid){

        Member targetMember = memberService.findById(memberID).toEntity();
        memberLeaveAbsenceService.delete(targetMember);
        memberInfoService.delete(targetMember);
        memberService.delete(memberID);

        return ResponseEntity.ok(new MemberIdResponseControllerDto(memberID));
    }

    // 다수 멤버 삭제
    @DeleteMapping
    public ResponseEntity<MessageDto> bulkDelMember(
            @RequestBody @Valid MemberBulkDeleteRequestControllerDto dto,
            @RequestHeader("user_pk") String uid){

        Set<String> memberIDs = dto.getMemberIDs();
        Set<Integer> memberInfoIDs = memberIDs.stream()
                .map(memberID -> {
                    return memberInfoService.findByMember(memberService.findById(memberID).toEntity()).toControllerDto().getId();
                })
                .collect(Collectors.toSet());

        Set<Integer> memberLeaveAbsenceIDs = memberIDs.stream()
                .map(memberID -> {
                    return memberLeaveAbsenceService.findByMember(memberService.findById(memberID).toEntity()).toControllerDto().getId();
                })
                .collect(Collectors.toSet());


        memberLeaveAbsenceService.delete(memberLeaveAbsenceIDs);
        memberInfoService.delete(memberInfoIDs);
        memberService.delete(dto.getMemberIDs());

        return ResponseEntity.ok(new MessageDto("총 ("+dto.getMemberIDs().size()+")개의 Member를 성공적으로 삭제했습니다!"));
    }

    // 단일 멤버 업데이트
    @PutMapping("{memberID}")
    public ResponseEntity<MemberInfoFullResponseControllerDto> updateMember(
            @PathVariable String memberID,
            @RequestBody @Valid MemberUpdateRequestControllerDto memberUpdateRequestControllerDto,
            @RequestHeader("user_pk") String uid){

        memberService.update(memberID, memberUpdateRequestControllerDto.toMemberServiceDto(
                roleService.findById(memberUpdateRequestControllerDto.getRoleId()).toEntity()));

        MemberInfoFullResponseControllerDto result = memberInfoService.update(memberService.findById(memberID).toEntity(), memberUpdateRequestControllerDto.toMemberInfoServiceDto(
                memberService.findById(memberID).toEntity(),
                majorService.findById(memberUpdateRequestControllerDto.getMajorId()).toEntity(),
                rankService.findById(memberUpdateRequestControllerDto.getRankId()).toEntity()),uid).toControllerDto();

        result.setStatus(memberService.findById(memberID).toEntity().isStatus());

        return ResponseEntity.ok(result);
    }

    // 다수 멤버 업데이트
    @PutMapping
    public ResponseEntity<MessageDto> bulkUpdateMember(
            @RequestBody @Valid @BulkUpdateMemberValid Set<MemberBulkUpdateRequestControllerDto> dtos,
            @RequestHeader("user_pk") String uid){

        memberService.updateAll(dtos.stream()
                                .map(MemberBulkUpdateRequestControllerDto::toMemberServiceDto)
                                .collect(Collectors.toList()));

        memberInfoService.updateAll(dtos.stream()
                .map(MemberBulkUpdateRequestControllerDto::toMemberInfoServiceDto)
                .collect(Collectors.toList()), uid);

        return ResponseEntity.ok(new MessageDto("총 ("+dtos.size()+")개의 Member를 성공적으로 업데이트했습니다!"));
    }
}
