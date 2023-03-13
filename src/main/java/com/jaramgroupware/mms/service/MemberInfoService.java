package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoRepository;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoAddRequestServiceDto;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 멤버정보에 관한 비즈니스 로직이 들어있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@Service
public class MemberInfoService {

    private final int batchSize = 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MemberInfoRepository memberInfoRepository;

    /**
     * 단일 멤버정보를 조회하기 위한 함수
     * @param member 조회하고자 하는 해당 멤버정보의 Member(Object)
     * @return 조회한 MemberInfo(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_MEMBER_INFO 예외 처리
     */
    @Transactional(readOnly = true)
    public MemberInfoResponseServiceDto findByMember(Member member){

        MemberInfo targetMemberInfo = memberInfoRepository.findMemberInfoByMember(member)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_INFO));

        return new MemberInfoResponseServiceDto(targetMemberInfo);
    }

    /**
     * 모든 멤버정보를 조회하기 위한 함수
     * @return 모든 MemberInfo(Object)의 정보를 담은 dto(List type), 해당 데이터가 없을 시 EMPTY_MEMBER_INFO 예외 처리
     */
    @Transactional(readOnly = true)
    public List<MemberInfoResponseServiceDto> findAll(){

        return memberInfoRepository.findAllBy()
                .orElseThrow(()->new CustomException(ErrorCode.EMPTY_MEMBER_INFO))
                .stream()
                .map(MemberInfoResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    /**
     * 모든 멤버정보를 조회하는 함수 (Query Options, Page Options)
     * @param specification query option
     * @param pageable sort option
     * @return 모든 MemberInfo(Object)의 정보를 담은 dto(List type)
     */
    @Transactional(readOnly = true)
    public List<MemberInfoResponseServiceDto> findAll(Specification<MemberInfo> specification, Pageable pageable){

        return memberInfoRepository.findAll(specification,pageable)
                .stream()
                .map(MemberInfoResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    /**
     * 모든 멤버정보를 조회하는 함수 (Query Options)
     * @param specification query option
     * @return 모든 MemberInfo(Object)의 정보를 담은 dto(List type)
     */
    @Transactional(readOnly = true)
    public List<MemberInfoResponseServiceDto> findAll(Specification<MemberInfo> specification){

        return memberInfoRepository.findAll(specification)
                .stream()
                .map(MemberInfoResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    /**
     * 단일 멤버정보를 등록하는 함수
     * @param memberInfoAddRequestServiceDto 등록할 MemberInfo(Object)의 정보를 담은 dto
     * @param who 해당 멤버정보를 생성한 Member(Object)의 UID(Firebase uid)
     * @return 등록한 MemberInfo(Object)의 ID, 등록 요청 dto 내의 학번이 이미 존재한다면 DUPLICATED_STUDENT_ID 예외 처리
     */
    @Transactional
    public Integer add(MemberInfoAddRequestServiceDto memberInfoAddRequestServiceDto, String who){

        if (memberInfoRepository.existsByStudentID(memberInfoAddRequestServiceDto.getStudentID())) {
            throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
        }

        MemberInfo targetMemberInfo = memberInfoAddRequestServiceDto.toEntity();
        targetMemberInfo.setCreateBy(who);
        targetMemberInfo.setModifiedBy(who);
        return memberInfoRepository.save(targetMemberInfo).getId();
    }

//    @Transactional
//    public void addAll(List<MemberInfoAddRequestServiceDto> memberInfoAddRequestServiceDtos, String who){
//        List<MemberInfo> memberInfos = new ArrayList<>();
//        for(MemberInfoAddRequestServiceDto dto : memberInfoAddRequestServiceDtos) {
//            if (memberInfoRepository.existsByStudentID(dto.getStudentID())) {
//                throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
//            }
//            dto.toEntity().setCreateBy(who);
//            dto.toEntity().setModifiedBy(who);
//            memberInfos.add(dto.toEntity());
//        }
//        memberInfoRepository.saveAll(memberInfos);
//    }

    /**
     * 단일 멤버정보를 삭제하는 함수
     * @param member 삭제할 하는 해당 멤버정보의 Member(Object)
     * @return 삭제된 MemberInfo(Object)의 ID, 해당 데이터가 없을 시 IllegalArgumentException 예외 처리
     */
    @Transactional
    public Integer delete(Member member){
        MemberInfo targetMemberInfo = memberInfoRepository.findMemberInfoByMember(member)
                .orElseThrow(()->new IllegalArgumentException(""));

        memberInfoRepository.delete(targetMemberInfo);

        return targetMemberInfo.getId();
    }

    /**
     * 다수의 멤버정보를 삭제하는 함수
     * @param ids 삭제할 MemberInfo(Object)의 ids(Array type)
     * @return 삭제된 MemberInfo(Object)의 ids(Array type)
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
     * 다수 멤버정보의 삭제를 일괄 처리하기 위한 함수
     * @param batchDto 다수의 삭제할 MemberInfo(Object)의 ID를 담고 있는 dtos(Array type)
     */
    private void batchDelete(Set<Integer> batchDto){
        if(memberInfoRepository.findAllByIdIn(batchDto).size() != batchDto.size())
            throw new IllegalArgumentException("찾을 수 없는 ID가 들어있습니다.");

        memberInfoRepository.deleteAllByIdInQuery(batchDto);
        batchDto.clear();
    }

    /**
     * 단일 멤버정보를 수정하는 함수
     * @param member 업데이트 대상 MemberInfo(Object)의 해당 Member(Object)
     * @param memberInfoUpdateRequestServiceDto MemberInfo(Object)의 수정 내용을 담은 dto
     * @param who MemberInfo(Object)를 수정한 Member(Object)의 UID(Firebase uid)
     * @return 수정된 MemberInfo(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_MEMBER_INFO 예외 처리
     */
    @Transactional
    public MemberInfoResponseServiceDto update(Member member, MemberInfoUpdateRequestServiceDto memberInfoUpdateRequestServiceDto, String who){

        MemberInfo targetMemberInfo = memberInfoRepository.findMemberInfoByMember(member)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_INFO));

        targetMemberInfo.update(memberInfoUpdateRequestServiceDto.toEntity(),who);

        memberInfoRepository.save(targetMemberInfo);

        return new MemberInfoResponseServiceDto(targetMemberInfo);
    }

//    @Transactional
//    public void updateAll(List<MemberInfoBulkUpdateRequestServiceDto> memberInfoUpdateRequestServiceDtos, String who){
//        List<MemberInfo> memberInfos = new ArrayList<>();
//        for(MemberInfoBulkUpdateRequestServiceDto dto : memberInfoUpdateRequestServiceDtos) {
//
//            MemberInfo targetMemberInfo = memberInfoRepository.findMemberInfoByMember(dto.getMember())
//                    .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_INFO));
//
//            targetMemberInfo.update(dto.toEntity(),who);
//            memberInfos.add(targetMemberInfo);
//        }
//        memberInfoRepository.saveAll(memberInfos);
//    }
}