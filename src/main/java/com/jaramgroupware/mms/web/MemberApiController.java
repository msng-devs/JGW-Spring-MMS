package com.jaramgroupware.mms.web;


import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberSpecification;
import com.jaramgroupware.mms.domain.member.MemberSpecificationBuilder;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoSpecification;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoSpecificationBuilder;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.general.controllerDto.MessageDto;
import com.jaramgroupware.mms.dto.member.controllerDto.*;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberResponseServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.controllerDto.MemberInfoFullResponseControllerDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoResponseServiceDto;
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
 * Member Api Controller 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/member")
public class MemberApiController {

    private final MemberService memberService;
    private final MemberInfoService memberInfoService;
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

    /**
     * 신규 Member를 가입시키는 함수
     * @param isNew 어떤 종류의 회원인지 구분, 새롭게 학회에 등록한 회원 = true, 기존 학회원 혹은 OB = false
     * @param memberRegisterRequestControllerDto Member(Object)와 MemberInfo(Object)의 신규 가입 요청을 담은 dto
     * @param uid 회원의 신규 가입을 요청한 Member(Object)의 UID(Firebase uid)
     * @return 신규 가입 완료된 Member(Object)의 UID(Firebase uid)를 반환
     */
    @PostMapping("/register")
    public ResponseEntity<MemberIdResponseControllerDto> registerMember(
            @RequestParam(defaultValue = "true",name = "isNew") Boolean isNew,
            @RequestBody @Valid MemberRegisterRequestControllerDto memberRegisterRequestControllerDto,
            @RequestHeader("user_pk") String uid){

        String id;
        if(isNew){
            id = memberService.add(memberRegisterRequestControllerDto.toServiceDto(defaultNewMemberRole));
            memberInfoService.add(memberRegisterRequestControllerDto.toServiceDto(defaultNewRank),"system");
        } else{
            id = memberService.add(memberRegisterRequestControllerDto.toServiceDto(defaultMemberRole));
            memberInfoService.add(memberRegisterRequestControllerDto.toServiceDto(defaultRank),"system");
        }

        return ResponseEntity.ok(new MemberIdResponseControllerDto(id));
    }

//    // 다수 Member를 등록
//    @PostMapping
//    public ResponseEntity<MessageDto> addMember(
//            @RequestBody @NotNull @BulkAddMemberValid Set<@Valid MemberAddRequestControllerDto> memberAddRequestControllerDto,
//            @RequestHeader("user_pk") String uid){
//
//        memberService.addAll(
//                memberAddRequestControllerDto.stream()
//                        .map(MemberAddRequestControllerDto::toMemberAddServiceDto)
//                        .collect(Collectors.toList()));
//
//        memberInfoService.addAll(
//                memberAddRequestControllerDto.stream()
//                        .map(MemberAddRequestControllerDto::toMemberInfoAddServiceDto)
//                        .collect(Collectors.toList()), uid);
//
//        return ResponseEntity.ok(new MessageDto("총 ("+memberAddRequestControllerDto.size()+")개의 Member를 성공적으로 추가했습니다!"));
//    }

    /**
     * 단일 Member와 MemberInfo를 등록하는 함수
     * @param dto Member(Object)와 MemberInfo(Object)의 등록 요청 정보를 담은 dto
     * @param uid 해당 등록을 요청한 Member(Object)의 UID(Firebase uid)
     * @return 새롭게 추가된 Member(Object)의 UID(Firebase uid)를 반환
     */
    @PostMapping
    public ResponseEntity<MemberIdResponseControllerDto> addMember(
            @RequestBody @Valid MemberAddRequestControllerDto dto,
            @RequestHeader("user_pk") String uid){

        String result = memberService.add(dto.toMemberAddServiceDto());
        memberInfoService.add(dto.toMemberInfoAddServiceDto(),uid);

        return ResponseEntity.ok(new MemberIdResponseControllerDto(result));
    }

    /**
     * 단일 Member를 조회하는 함수
     * @param memberId 조회할 Member(Object)의 UID(Firebase uid)
     * @param uid 해당 조회를 요청한 Member(Object)의 UID(Firebase uid)
     * @param roleID 해당 조회를 요청한 Member(Object)의 Role(Object) ID
     * @return 성공적으로 조회 완료 시 해당 Member(Object)의 정보를 담은 dto 반환,
     * 권한이 없는 유저가 자기 자신이 아닌 타 유저의 정보를 조회할 시 일부 정보를 담은 dto 반환
     */
    @GetMapping("{memberId}")
    public ResponseEntity<?> getMemberById(
            @PathVariable String memberId,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID){

        MemberFullResponseControllerDto result = memberService.findById(memberId).toController();

        if(roleID >= adminRole.getId() || uid.equals(memberId)) return ResponseEntity.ok(result);

        return ResponseEntity.ok(result.toTiny());
    }

    /**
     * 단일 MemberInfo를 조회하는 함수
     * @param memberId 조회할 MemberInfo(Object)의 해당 Member(Object) UID(Firebase uid)
     * @param uid 해당 조회를 요청한 Member(Object)의 UID(Firebase uid)
     * @param roleID 해당 조회를 요청한 Member(Object)의 Role(Object) ID
     * @return 성공적으로 조회 완료 시 해당 Member(Object)와 MemberInfo(Object)의 정보를 담은 dto를 반환,
     * 권한이 없는 유저가 자기 자신이 아닌 타 유저의 정보를 조회할 시 일부 정보를 담은 dto 반환
     */
    @GetMapping("/info/{memberId}")
    public ResponseEntity<?> getMemberInfoById(
            @PathVariable String memberId,
            @RequestHeader("user_pk") String uid,
            @RequestHeader("role_pk") Integer roleID){

        Member member = memberService.findById(memberId).toEntity();
        MemberInfoFullResponseControllerDto result = memberInfoService.findByMember(member).toControllerDto();

        if(roleID >= adminRole.getId() || uid.equals(memberId)) return ResponseEntity.ok(result);

        return ResponseEntity.ok(result.toTiny());
    }

    /**
     * 다수 Member를 조회하는 함수
     * @param pageable sort option
     * @param queryParam query option
     * @param includeGuest 신규 학회원(true), 기존 학회원(false) option
     * @param uid 다수 Member(Object)의 조회를 요청한 Member(Object)의 UID(Firebase uid)
     * @return 다수 Member(Object)의 정보를 담은 dto들을 반환(List type)
     */
    @GetMapping
    public ResponseEntity<List<MemberFullResponseControllerDto>> getMemberAll(
            @PageableDefault(page = 0,size = 1000,sort = "id",direction = Sort.Direction.DESC)
            @PageableValid(sortKeys =
                    {"id","email","name","role","status"}
            ) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> queryParam,
            @RequestParam(required = false,defaultValue = "false") Boolean includeGuest,
            @RequestHeader("user_pk") String uid){

        logger.debug("includeGuest {}", includeGuest);

        //limit 확인 및 추가
        int limit = queryParam.containsKey("limit") ? Integer.parseInt(Objects.requireNonNull(queryParam.getFirst("limit"))) : -1;

        //Specification 등록
        MemberSpecification spec = memberSpecificationBuilder.toSpec(queryParam);

        List<MemberFullResponseControllerDto> results;

        //limit true
        if(limit > 0){
            results = memberService.findAll(spec, PageRequest.of(0, limit, pageable.getSort()))
                    .stream()
                    .map(MemberResponseServiceDto::toController)
                    .collect(Collectors.toList());
        }

        else{
            results = memberService.findAll(spec,pageable)
                    .stream()
                    .map(MemberResponseServiceDto::toController)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(results);
    }

    /**
     * 다수 MemberInfo를 조회하는 함수
     * @param pageable sort option
     * @param queryParam query option
     * @param includeGuest 신규 학회원(true), 기존 학회원(false) option
     * @param uid 다수 MemberInfo(Object)의 조회를 요청한 Member(Object)의 UID(Firebase uid)
     * @return 다수 Member(Object)와 MemberInfo(Object)의 정보를 담은 dto들을 반환(List type)
     */
    @GetMapping("/info")
    public ResponseEntity<List<MemberInfoFullResponseControllerDto>> getMemberInfoAll(
            @PageableDefault(page = 0,size = 1000,sort = "id",direction = Sort.Direction.DESC)
            @PageableValid(sortKeys =
                    {"id","phoneNumber","studentID","major","rank","year","dateOfBirth","createdDateTime","modifiedDateTime","createBy","modifiedBy"}
            ) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> queryParam,
            @RequestParam(required = false,defaultValue = "false") Boolean includeGuest,
            @RequestHeader("user_pk") String uid){

        logger.debug("includeGuest {}", includeGuest);

        //limit 확인 및 추가
        int limit = queryParam.containsKey("limit") ? Integer.parseInt(Objects.requireNonNull(queryParam.getFirst("limit"))) : -1;

        //Specification 등록
        MemberInfoSpecification spec = memberInfoSpecificationBuilder.toSpec(queryParam);

        List<MemberInfoFullResponseControllerDto> results;

        //limit true
        if(limit > 0){
            results = memberInfoService.findAll(spec, PageRequest.of(0, limit, pageable.getSort()))
                    .stream()
                    .map(MemberInfoResponseServiceDto::toControllerDto)
                    .collect(Collectors.toList());
        }

        else{
            results = memberInfoService.findAll(spec,pageable)
                    .stream()
                    .map(MemberInfoResponseServiceDto::toControllerDto)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(results);
    }

    /**
     * 단일 Member를 삭제하는 함수
     * @param memberID 삭제할 Member(Object)의 UID(Firebase uid)
     * @param uid 해당 Member(Object)의 삭제를 요청한 Member(Object)의 UID(Firebase uid)
     * @return 삭제된 Member(Object)의 ID 정보를 담은 dto를 반환
     */
    @DeleteMapping("{memberID}")
    public ResponseEntity<MemberIdResponseControllerDto> delMember(
            @PathVariable String memberID,
            @RequestHeader("user_pk") String uid){

        memberService.delete(memberID);

        return ResponseEntity.ok(new MemberIdResponseControllerDto(memberID));
    }

    /**
     * 다수 Member를 삭제하는 함수
     * @param dto 삭제할 다수 Member(Object)의 ID 정보를 담은 dto
     * @param uid 해당 Member(Object)의 삭제를 요청한 Member(Object)의 UID(Firebase uid)
     * @return 삭제된 Member(Object)의 개수를 포함한 메시지 반환
     */
    @DeleteMapping
    public ResponseEntity<MessageDto> bulkDelMember(
            @RequestBody @Valid MemberBulkDeleteRequestControllerDto dto,
            @RequestHeader("user_pk") String uid){

        memberService.delete(dto.getMemberIDs());

        return ResponseEntity.ok(new MessageDto("총 ("+dto.getMemberIDs().size()+")개의 Member를 성공적으로 삭제했습니다!"));
    }

    /**
     * 단일 Member와 MemberInfo를 수정하는 함수
     * @param memberID 수정할 Member(Object)의 UID(Firebase uid)
     * @param memberUpdateRequestControllerDto 수정할 Member(Object)와 MemberInfo(Object)의 수정 정보를 담은 dto
     * @param uid 해당 수정을 요청한 Member(Object)의 UID(Firebase uid)
     * @return 수정된 Member(Object)와 MemberInfo(Object)의 정보를 담은 dto 반환
     */
    @PutMapping("{memberID}")
    public ResponseEntity<MemberInfoFullResponseControllerDto> updateMember(
            @PathVariable String memberID,
            @RequestBody @Valid MemberUpdateRequestControllerDto memberUpdateRequestControllerDto,
            @RequestHeader("user_pk") String uid){

        Member member = memberService.findById(memberID).toEntity();
        Role role = roleService.findById(memberUpdateRequestControllerDto.getRoleId()).toEntity();
        Major major = majorService.findById(memberUpdateRequestControllerDto.getMajorId()).toEntity();
        Rank rank = rankService.findById(memberUpdateRequestControllerDto.getRankId()).toEntity();

        memberService.update(memberID, memberUpdateRequestControllerDto.toMemberServiceDto(role));
        MemberInfoFullResponseControllerDto result = memberInfoService.update(member, memberUpdateRequestControllerDto.toMemberInfoServiceDto(member,major,rank), uid).toControllerDto();

        return ResponseEntity.ok(result);
    }

//    // 다수 멤버 업데이트
//    @PutMapping
//    public ResponseEntity<MessageDto> bulkUpdateMember(
//            @RequestBody @Valid @BulkUpdateMemberValid Set<MemberBulkUpdateRequestControllerDto> dtos,
//            @RequestHeader("user_pk") String uid){
//
//        memberService.updateAll(dtos.stream()
//                                .map(MemberBulkUpdateRequestControllerDto::toMemberServiceDto)
//                                .collect(Collectors.toList()));
//
//        memberInfoService.updateAll(dtos.stream()
//                .map(MemberBulkUpdateRequestControllerDto::toMemberInfoServiceDto)
//                .collect(Collectors.toList()), uid);
//
//        return ResponseEntity.ok(new MessageDto("총 ("+dtos.size()+")개의 Member를 성공적으로 업데이트했습니다!"));
//    }
}
