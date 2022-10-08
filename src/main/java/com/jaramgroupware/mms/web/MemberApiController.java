package com.jaramgroupware.mms.web;


import com.jaramgroupware.mms.domain.member.MemberSpecification;
import com.jaramgroupware.mms.domain.member.MemberSpecificationBuilder;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.general.controllerDto.MessageDto;
import com.jaramgroupware.mms.dto.member.controllerDto.*;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberResponseServiceDto;
import com.jaramgroupware.mms.service.*;
import com.jaramgroupware.mms.utils.validation.PageableValid;
import com.jaramgroupware.mms.utils.validation.member.BulkAddMemberValid;
import com.jaramgroupware.mms.utils.validation.member.BulkUpdateMemberValid;
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
import javax.validation.constraints.NotNull;
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
    private final MajorService majorService;
    private final RankService rankService;
    private final RoleService roleService;
    private final ConfigService configService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MemberSpecificationBuilder memberSpecificationBuilder;

    private final Role defaultMemberRole = Role.builder().id(1).build();
    private final Role defaultNewMemberRole = Role.builder().id(0).build();

    private final Rank defaultRank = Rank.builder().id(1).build();
    private final Rank defaultNewRank = Rank.builder().id(2).build();

    private final Role adminRole = Role.builder().id(4).build();


    @PostMapping("/register")
    public ResponseEntity<MemberIdResponseControllerDto> registerMember(
            @RequestParam(defaultValue = "true",name = "isNew") Boolean isNew,
            @RequestBody @Valid MemberRegisterRequestControllerDto memberRegisterRequestControllerDto,
            @RequestHeader("user_uid") String uid){

        String id;
        if(isNew){
            id = memberService.add(memberRegisterRequestControllerDto.toServiceDto(defaultNewRank,defaultNewMemberRole),"system");
        } else{
            id = memberService.add(memberRegisterRequestControllerDto.toServiceDto(defaultRank,defaultMemberRole),"system");
        }

        return ResponseEntity.ok(new MemberIdResponseControllerDto(id));
    }

    @PostMapping
    public ResponseEntity<MessageDto> addMember(
            @RequestBody @NotNull @BulkAddMemberValid Set<@Valid MemberAddRequestControllerDto> memberAddRequestControllerDto,
            @RequestHeader("user_uid") String uid){

        memberService.add(
                memberAddRequestControllerDto.stream()
                        .map(MemberAddRequestControllerDto::toServiceDto)
                        .collect(Collectors.toList()), uid);

        return ResponseEntity.ok(new MessageDto("총 ("+memberAddRequestControllerDto.size()+")개의 Member를 성공적으로 추가했습니다!"));
    }
    //TODO role 조회하여 admin 권한 이상이면 전체 정보를, 아닐 경우 일부 정보(이름 학과 학번(일부) 기수 Email)
    @GetMapping("{memberId}")
    public ResponseEntity<?> getMemberById(
            @PathVariable String memberId,
            @RequestHeader("user_uid") String uid,
            @RequestHeader("user_role_id") Integer roleID){

        MemberFullResponseControllerDto result = memberService.findById(memberId).toControllerDto();
        if(roleID >= adminRole.getId() || uid.equals(memberId)) return ResponseEntity.ok(result);

        return ResponseEntity.ok(result.toTiny());
    }

    @GetMapping
    public ResponseEntity<List<MemberFullResponseControllerDto>> getMemberAll(
            @PageableDefault(page = 0,size = 1000,sort = "id",direction = Sort.Direction.DESC)
            @PageableValid(sortKeys =
                    {"id","email","name","phoneNumber","studentID","major","rank","role","year","leaveAbsence","dateOfBirth","createdDateTime","modifiedDateTime","createBy","modifiedBy"}
            ) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> queryParam,
            @RequestParam(required = false,defaultValue = "false") Boolean includeGuest,
            @RequestHeader("user_uid") String uid){

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
                    .map(MemberResponseServiceDto::toControllerDto)
                    .collect(Collectors.toList());
        }

        else{
            results = memberService.findAll(spec,pageable)
                    .stream()
                    .map(MemberResponseServiceDto::toControllerDto)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(results);
    }


    @DeleteMapping("{memberID}")
    public ResponseEntity<MemberIdResponseControllerDto> delMember(
            @PathVariable String memberID,
            @RequestHeader("user_uid") String uid){

        memberService.delete(memberID);

        return ResponseEntity.ok(new MemberIdResponseControllerDto(memberID));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> bulkDelMember(
            @RequestBody @Valid MemberBulkDeleteRequestControllerDto dto,
            @RequestHeader("user_uid") String uid){

        memberService.delete(dto.getMemberIDs());

        return ResponseEntity.ok(new MessageDto("총 ("+dto.getMemberIDs().size()+")개의 Member를 성공적으로 삭제했습니다!"));
    }

    @PutMapping("{memberID}")
    public ResponseEntity<MemberFullResponseControllerDto> updateMember(
            @PathVariable String memberID,
            @RequestBody @Valid MemberUpdateRequestControllerDto memberUpdateRequestControllerDto,
            @RequestHeader("user_uid") String uid){

        MemberFullResponseControllerDto result = memberService.update(memberID,
                memberUpdateRequestControllerDto.toServiceDto(
                        majorService.findById(memberUpdateRequestControllerDto.getMajorId()).toEntity(),
                        rankService.findById(memberUpdateRequestControllerDto.getRankId()).toEntity(),
                        roleService.findById(memberUpdateRequestControllerDto.getRoleId()).toEntity()
                ),uid).toControllerDto();

        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<MessageDto> bulkUpdateMember(
            @RequestBody @Valid @BulkUpdateMemberValid Set<MemberBulkUpdateRequestControllerDto> dtos,
            @RequestHeader("user_uid") String uid){

        memberService.update(dtos.stream()
                                .map(MemberBulkUpdateRequestControllerDto::toServiceDto)
                                .collect(Collectors.toList()), uid);

        return ResponseEntity.ok(new MessageDto("총 ("+dtos.size()+")개의 Member를 성공적으로 업데이트했습니다!"));
    }
}
