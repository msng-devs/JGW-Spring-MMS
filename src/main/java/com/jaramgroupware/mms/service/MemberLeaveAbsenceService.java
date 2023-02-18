package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceResponseServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceUpdateRequestServiceDto;
import com.jaramgroupware.mms.utils.exception.CustomException;
import com.jaramgroupware.mms.utils.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberLeaveAbsenceService {

    private final int batchSize = 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    @Transactional(readOnly = true)
    public MemberLeaveAbsenceResponseServiceDto findById(String uid){

        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceById(uid)
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

    @Transactional
    public String add(MemberLeaveAbsenceAddRequestServiceDto memberLeaveAbsenceAddRequestServiceDto){
        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceAddRequestServiceDto.toEntity();
        return memberLeaveAbsenceRepository.save(targetMemberLeaveAbsence).getId();
    }

    @Transactional
    public String delete(String id){
        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceById(id)
                .orElseThrow(()->new IllegalArgumentException(""));

        memberLeaveAbsenceRepository.delete(targetMemberLeaveAbsence);

        return id;
    }

    @Transactional
    public MemberLeaveAbsenceResponseServiceDto update(String id, MemberLeaveAbsenceUpdateRequestServiceDto memberLeaveAbsenceUpdateRequestServiceDto){

        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceById(id)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_LEAVE_ABSENCE_ID));

        targetMemberLeaveAbsence.update(memberLeaveAbsenceUpdateRequestServiceDto.toEntity());

        memberLeaveAbsenceRepository.save(targetMemberLeaveAbsence);

        return new MemberLeaveAbsenceResponseServiceDto(targetMemberLeaveAbsence);
    }


}
