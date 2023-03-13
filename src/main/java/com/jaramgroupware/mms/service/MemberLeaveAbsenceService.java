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

/**
 * 멤버 휴학 정보에 관한 비즈니스 로직이 들어있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@Service
public class MemberLeaveAbsenceService {

    private final int batchSize = 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    /**
     * 단일 멤버 휴학 정보를 조회하는 함수
     * @param id 조회할 MemberLeaveAbsence(Object)의 ID
     * @return 조회된 MemberLeaveAbsence(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_MEMBER_LEAVE_ABSENCE_ID 예외 처리
     */
    @Transactional(readOnly = true)
    public MemberLeaveAbsenceResponseServiceDto findById(Integer id){

        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceById(id)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_LEAVE_ABSENCE_ID));

        return new MemberLeaveAbsenceResponseServiceDto(targetMemberLeaveAbsence);
    }

    /**
     * 단일 멤버 휴학 정보를 조회하는 함수
     * @param member 조회할 멤버 휴학 정보의 해당 Member(Object)
     * @return 조회된 MemberLeaveAbsence(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_MEMBER_LEAVE_ABSENCE_ID 예외 처리
     */
    @Transactional(readOnly = true)
    public MemberLeaveAbsenceResponseServiceDto findByMember(Member member){

        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceByMember(member)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_LEAVE_ABSENCE_ID));

        return new MemberLeaveAbsenceResponseServiceDto(targetMemberLeaveAbsence);
    }

    /**
     * 모든 멤버 휴학 정보를 조회하는 함수
     * @return 모든 MemberLeaveAbsence(Object)의 정보를 담은 dto(List type), 해당 데이터가 없을 시 EMPTY_MEMBER_LEAVE_ABSENCE 예외 처리
     */
    @Transactional(readOnly = true)
    public List<MemberLeaveAbsenceResponseServiceDto> findAll(){

        return memberLeaveAbsenceRepository.findAllBy()
                .orElseThrow(()->new CustomException(ErrorCode.EMPTY_MEMBER_LEAVE_ABSENCE))
                .stream()
                .map(MemberLeaveAbsenceResponseServiceDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 모든 멤버 휴학 정보를 조회하는 함수 (Query Options, Page Options)
     * @param specification query option
     * @param pageable sort option
     * @return 모든 MemberLeaveAbsence(Object)의 정보를 담은 dto(List type)
     */
    @Transactional(readOnly = true)
    public List<MemberLeaveAbsenceResponseServiceDto> findAll(Specification<MemberLeaveAbsence> specification, Pageable pageable){

        return memberLeaveAbsenceRepository.findAll(specification,pageable)
                .stream()
                .map(MemberLeaveAbsenceResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    /**
     * 모든 멤버 휴학 정보를 조회하는 함수 (Query Options)
     * @param specification query option
     * @return 모든 MemberLeaveAbsence(Object)의 정보를 담은 dto(List type)
     */
    @Transactional(readOnly = true)
    public List<MemberLeaveAbsenceResponseServiceDto> findAll(Specification<MemberLeaveAbsence> specification){

        return memberLeaveAbsenceRepository.findAll(specification)
                .stream()
                .map(MemberLeaveAbsenceResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    /**
     * 단일 멤버 휴학 정보를 등록하는 함수
     * @param memberLeaveAbsenceAddRequestServiceDto 등록할 멤버 휴학 정보를 담은 dto
     * @return 등록된 멤버 휴학 정보의 ID
     */
    @Transactional
    public Integer add(MemberLeaveAbsenceAddRequestServiceDto memberLeaveAbsenceAddRequestServiceDto){
        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceAddRequestServiceDto.toEntity();
        return memberLeaveAbsenceRepository.save(targetMemberLeaveAbsence).getId();
    }

    /**
     * 단일 멤버 휴학 정보를 삭제하는 함수
     * @param member 삭제할 멤버 휴학 정보의 해당 Member(Object)
     * @return 삭제된 멤버 휴학 정보의 ID, 해당 데이터 없을 시 IllegalArgumentException 예외 처리
     */
    @Transactional
    public Integer delete(Member member){
        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceByMember(member)
                .orElseThrow(()->new IllegalArgumentException(""));

        memberLeaveAbsenceRepository.delete(targetMemberLeaveAbsence);

        return targetMemberLeaveAbsence.getId();
    }

    /**
     * 다수의 멤버 휴학 정보를 삭제하는 함수
     * @param ids 삭제할 MemberLeaveAbsence(Object)의 ids(Array type)
     * @return 삭제된 MemberLeaveAbsence(Object)의 ids(Array type)
     */
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

    /**
     * 다수 멤버 휴학 정보의 삭제를 일괄 처리하기 위한 함수
     * @param batchDto 다수의 삭제할 MemberLeaveAbsence(Object)의 id를 담고 있는 dtos(Array type)
     */
    private void batchDelete(Set<Integer> batchDto){
        if(memberLeaveAbsenceRepository.findAllByIdIn(batchDto).size() != batchDto.size())
            throw new IllegalArgumentException("찾을 수 없는 ID가 들어있습니다.");

        memberLeaveAbsenceRepository.deleteAllByIdInQuery(batchDto);
        batchDto.clear();
    }

    /**
     * 단일 멤버 휴학 정보를 수정하는 함수
     * @param member 업데이트 대상 멤버 휴학 정보의 해당 Member(Object)
     * @param memberLeaveAbsenceUpdateRequestServiceDto MemberLeaveAbsence(Object)의 수정 내용을 담은 dto
     * @return 수정된 MemberLeaveAbsence(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_MEMBER_LEAVE_ABSENCE_ID 예외 처리
     */
    @Transactional
    public MemberLeaveAbsenceResponseServiceDto update(Member member, MemberLeaveAbsenceUpdateRequestServiceDto memberLeaveAbsenceUpdateRequestServiceDto){

        MemberLeaveAbsence targetMemberLeaveAbsence = memberLeaveAbsenceRepository.findMemberLeaveAbsenceByMember(member)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_LEAVE_ABSENCE_ID));

        targetMemberLeaveAbsence.update(memberLeaveAbsenceUpdateRequestServiceDto.toEntity());

        memberLeaveAbsenceRepository.save(targetMemberLeaveAbsence);

        return new MemberLeaveAbsenceResponseServiceDto(targetMemberLeaveAbsence);
    }


}
