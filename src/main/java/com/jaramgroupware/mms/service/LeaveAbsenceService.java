package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.MemberLeaveAbsenceResponseDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceUpdateRequestServiceDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LeaveAbsenceService {

    private final MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberLeaveAbsenceResponseDto findByMember(String memberId){
        var targetMember = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 회원입니다."));
        var targetLeaveAbsence = memberLeaveAbsenceRepository.findByMember(targetMember)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR,"휴학 정보가 존재하지 않습니다. 관리자에게 문의하세요."));

        return new MemberLeaveAbsenceResponseDto(targetLeaveAbsence);
    }

    @Transactional(readOnly = true)
    public MemberLeaveAbsenceResponseDto update(MemberLeaveAbsenceUpdateRequestServiceDto dto){
        var targetMember = memberRepository.findMemberById(dto.getUid())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 회원입니다."));

        var targetLeaveAbsence = memberLeaveAbsenceRepository.findByMember(targetMember)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR,"휴학 정보가 존재하지 않습니다. 관리자에게 문의하세요."));

        targetLeaveAbsence.update(dto.toEntity());
        memberLeaveAbsenceRepository.save(targetLeaveAbsence);

        var newMemberLeaveAbsence = memberLeaveAbsenceRepository.save(targetLeaveAbsence);
        return new MemberLeaveAbsenceResponseDto(newMemberLeaveAbsence);
    }
}
