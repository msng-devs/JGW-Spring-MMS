package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoResponseServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceResponseServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceUpdateRequestServiceDto;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberLeaveAbsenceService {

    private final int batchSize = 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    @Transactional(readOnly = true)
    public MemberLeaveAbsenceResponseServiceDto findById(Integer id){

        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceById(id)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_TIMETABLE_ID));

        return new MemberLeaveAbsenceResponseServiceDto(targetMemberLeaveAbsence);
    }

    @Transactional(readOnly = true)
    public List<MemberLeaveAbsenceResponseServiceDto> findAll(){

        return memberLeaveAbsenceRepository.findAllBy()
                .orElseThrow(()->new CustomException(ErrorCode.EMPTY_MEMBER_LEAVE_ABSENCE))
                .stream()
                .map(MemberLeaveAbsenceResponseServiceDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberLeaveAbsenceResponseServiceDto> findAll(Specification<MemberLeaveAbsence> specification, Pageable pageable){

        return memberLeaveAbsenceRepository.findAll(specification,pageable)
                .stream()
                .map(MemberLeaveAbsenceResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<MemberLeaveAbsenceResponseServiceDto> findAll(Specification<MemberLeaveAbsence> specification){

        return memberLeaveAbsenceRepository.findAll(specification)
                .stream()
                .map(MemberLeaveAbsenceResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public MemberLeaveAbsenceResponseServiceDto findByMember(Member member){

        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceByMember(member)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_INFO));

        return new MemberLeaveAbsenceResponseServiceDto(targetMemberLeaveAbsence);
    }

    @Transactional
    public Integer add(MemberLeaveAbsenceAddRequestServiceDto memberLeaveAbsenceAddRequestServiceDto){
        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceAddRequestServiceDto.toEntity();
        return memberLeaveAbsenceRepository.save(targetMemberLeaveAbsence).getId();
    }

    @Transactional
    public Integer delete(Member member){
        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceByMember(member)
                .orElseThrow(()->new IllegalArgumentException(""));

        memberLeaveAbsenceRepository.delete(targetMemberLeaveAbsence);

        return targetMemberLeaveAbsence.getId();
    }

    @Transactional
    public Set<Integer> delete(Set<Integer> ids){
        Set<Integer> batchDto = new HashSet<>();
        for (Integer id : ids) {
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

    private void batchDelete(Set<Integer> batchDto){
        if(memberLeaveAbsenceRepository.findAllByIdIn(batchDto).size() != batchDto.size())
            throw new IllegalArgumentException("찾을 수 없는 ID가 들어있습니다.");

        memberLeaveAbsenceRepository.deleteAllByIdInQuery(batchDto);
        batchDto.clear();
    }

    @Transactional
    public MemberLeaveAbsenceResponseServiceDto update(Member member, MemberLeaveAbsenceUpdateRequestServiceDto memberLeaveAbsenceUpdateRequestServiceDto){

        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceByMember(member)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_LEAVE_ABSENCE_ID));

        targetMemberLeaveAbsence.update(memberLeaveAbsenceUpdateRequestServiceDto.toEntity());

        memberLeaveAbsenceRepository.save(targetMemberLeaveAbsence);

        return new MemberLeaveAbsenceResponseServiceDto(targetMemberLeaveAbsence);
    }


}
