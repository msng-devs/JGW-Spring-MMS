package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoRepository;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberAddRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberBulkUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberResponseServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoBulkUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoResponseServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoUpdateRequestServiceDto;
import com.jaramgroupware.mms.utils.exception.CustomException;
import com.jaramgroupware.mms.utils.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class MemberInfoService {

    private final int batchSize = 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MemberInfoRepository memberInfoRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    @Autowired
    private final RankRepository rankRepository;

    @Autowired
    private final MajorRepository majorRepository;

    @Transactional(readOnly = true)
    public MemberInfoResponseServiceDto findById(String uid){

        MemberInfo targetMemberInfo = memberInfoRepository.findMemberInfoById(uid)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_INFO_ID));

        return new MemberInfoResponseServiceDto(targetMemberInfo);
    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponseServiceDto> findAll(){

        return memberInfoRepository.findAllBy()
                .orElseThrow(()->new CustomException(ErrorCode.EMPTY_MEMBER_INFO))
                .stream()
                .map(MemberInfoResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponseServiceDto> findAll(Specification<MemberInfo> specification, Pageable pageable){

        return memberInfoRepository.findAll(specification,pageable)
                .stream()
                .map(MemberInfoResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponseServiceDto> findAll(Specification<MemberInfo> specification){

        return memberInfoRepository.findAll(specification)
                .stream()
                .map(MemberInfoResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public String add(MemberInfoAddRequestServiceDto memberInfoAddRequestServiceDto, String who){
        if(memberInfoRepository.existsByStudentID(memberInfoAddRequestServiceDto.getStudentId())) {
            throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
        }
        MemberInfo targetMemberInfo = memberInfoAddRequestServiceDto.toEntity();
        targetMemberInfo.setCreateBy(who);
        targetMemberInfo.setModifiedBy(who);
        return memberInfoRepository.save(targetMemberInfo).getId();
    }

    @Transactional
    public void add(List<MemberInfoAddRequestServiceDto> memberInfoAddRequestServiceDto,String who){
        List<MemberInfoAddRequestServiceDto> batchDto = new ArrayList<>();
        for (MemberInfoAddRequestServiceDto dto:memberInfoAddRequestServiceDto) {
            if(memberInfoRepository.existsByStudentID(dto.getStudentId())) {
                throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
            }
            batchDto.add(dto);
            if(batchDto.size() == batchSize){
                batchAdd(batchDto,who);
            }
        }
        if(!batchDto.isEmpty()) {
            batchAdd(batchDto,who);
        }
    }

    public void batchAdd(List<MemberInfoAddRequestServiceDto> batchDto,String who){
        memberInfoRepository.bulkInsert(batchDto.stream().map(MemberInfoAddRequestServiceDto::toEntity).collect(Collectors.toList()),who);
        batchDto.clear();
    }

    @Transactional
    public String delete(String id){
        MemberInfo targetMemberInfo = memberInfoRepository.findMemberInfoById(id)
                .orElseThrow(()->new IllegalArgumentException(""));

        memberInfoRepository.delete(targetMemberInfo);

        return id;
    }

    @Transactional
    public Set<String> delete(Set<String> ids){
        Set<String> batchDto = new HashSet<>();
        for (String id : ids) {
            batchDto.add(id);
            if(batchDto.size() == batchSize){
                batchDelete(batchDto);
            }
        }
        if(!batchDto.isEmpty()) {
            batchDelete(batchDto);
        }

        return ids;
    }

    private void batchDelete(Set<String> batchDto){
        if(memberInfoRepository.findAllByIdIn(batchDto).size() != batchDto.size())
            throw new IllegalArgumentException("찾을 수 없는 ID가 들어있습니다.");

        memberInfoRepository.deleteAllByIdInQuery(batchDto);
        batchDto.clear();
    }

    @Transactional
    public MemberInfoResponseServiceDto update(String id, MemberInfoUpdateRequestServiceDto memberInfoUpdateRequestServiceDto, String who){

        MemberInfo targetMemberInfo = memberInfoRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_INFO_ID));

        targetMemberInfo.update(memberInfoUpdateRequestServiceDto.toEntity(),who);

        memberInfoRepository.save(targetMemberInfo);

        return new MemberInfoResponseServiceDto(targetMemberInfo);
    }

    @Transactional
    public void update(List<MemberInfoBulkUpdateRequestServiceDto> updateDtos, String who){
        List<MemberInfoBulkUpdateRequestServiceDto> batchDto = new ArrayList<>();
        for (MemberInfoBulkUpdateRequestServiceDto dto:updateDtos) {
            batchDto.add(dto);
            if(batchDto.size() == batchSize){
                batchUpdate(batchDto,who);
            }
        }
        if(!batchDto.isEmpty()) {
            batchUpdate(batchDto,who);
        }
    }

    private void batchUpdate(List<MemberInfoBulkUpdateRequestServiceDto> batchDto,String who){
        Set<String> ids = batchDto.stream().map(MemberInfoBulkUpdateRequestServiceDto::getId).collect(Collectors.toSet());
        if(memberInfoRepository.findAllByIdIn(ids).size() != ids.size())
            throw new IllegalArgumentException("찾을 수 없는 ID가 들어있습니다.");
        memberInfoRepository.bulkUpdate(batchDto.stream().map(MemberInfoBulkUpdateRequestServiceDto::toEntity).collect(Collectors.toList()),who);
        batchDto.clear();
        ids.clear();
    }

}

