package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoRepository;
import com.jaramgroupware.mms.dto.member.serviceDto.*;
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
 * 멤버에 관한 비즈니스 로직이 들어있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@Service
public class MemberService {

    private final int batchSize = 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final MemberInfoRepository memberInfoRepository;

    /**
     * 단일 멤버를 조회하는 함수
     * @param uid 조회할 Member(Object)의 UID(Firebase uid)
     * @return 조회된 Member(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_MEMBER_ID 예외 처리
     */
    @Transactional(readOnly = true)
    public MemberResponseServiceDto findById(String uid){

        Member targetMember = memberRepository.findMemberById(uid)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_ID));

        return new MemberResponseServiceDto(targetMember);
    }

    /**
     * 모든 멤버를 조회하는 함수
     * @return 모든 Member(Object)의 정보를 담은 dto(List type), 해당 데이터가 없을 시 EMPTY_MEMBER 예외 처리
     */
    @Transactional(readOnly = true)
    public List<MemberResponseServiceDto> findAll(){

        return memberRepository.findAllBy()
                .orElseThrow(()->new CustomException(ErrorCode.EMPTY_MEMBER))
                .stream()
                .map(MemberResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    /**
     * 모든 멤버를 조회하는 함수 (Query Options, Page Options)
     * @param specification query option
     * @param pageable sort option
     * @return 모든 Member(Object)의 정보를 담은 dto(List type)
     */
    @Transactional(readOnly = true)
    public List<MemberResponseServiceDto> findAll(Specification<Member> specification, Pageable pageable){

        return memberRepository.findAll(specification,pageable)
                .stream()
                .map(MemberResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    /**
     * 모든 멤버를 조회하는 함수 (Query Options)
     * @param specification query option
     * @return 모든 Member(Object)의 정보를 담은 dto(List type)
     */
    @Transactional(readOnly = true)
    public List<MemberResponseServiceDto> findAll(Specification<Member> specification){

        return memberRepository.findAll(specification)
                .stream()
                .map(MemberResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    /**
     * 신규 회원을 최종으로 등록 완료하는 함수
     * @param memberRegisterRequestServiceDto 신규 회원의 등록 정보를 담은 dto
     * @return 등록된 Member(Object)의 UID(Firebase uid), email 중복시 DUPLICATED_EMAIL 예외 처리
     */
    @Transactional
    public String register(MemberRegisterRequestServiceDto memberRegisterRequestServiceDto) {

        if (memberRepository.existsByEmail(memberRegisterRequestServiceDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        Member targetMember = memberRegisterRequestServiceDto.toEntity();
        Member result = memberRepository.save(targetMember);

        MemberInfo targetMemberInfo = memberRegisterRequestServiceDto.getMemberInfo();
        targetMemberInfo.setMember(result);
        memberInfoRepository.save(targetMemberInfo);

        return result.getId();
    }

    /**
     * 단일 멤버를 등록하는 함수
     * @param memberAddRequestServiceDto 등록할 멤버 정보를 담은 dto
     * @return 등록된 Member(Object)의 UID(Firebase uid), 등록 요청 dto 내의 이메일이 이미 존재할 경우 DUPLICATED_EMAIL 예외 처리
     */
    @Transactional
    public String add(MemberAddRequestServiceDto memberAddRequestServiceDto){

        if(memberRepository.existsByEmail(memberAddRequestServiceDto.getEmail())) {
            throw  new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        Member targetMember = memberAddRequestServiceDto.toEntity();
        return memberRepository.save(targetMember).getId();
    }

//    @Transactional
//    public void addAll(List<MemberAddRequestServiceDto> memberAddRequestServiceDtos){
//
//        List<Member> members = new ArrayList<>();
//        for(MemberAddRequestServiceDto dto : memberAddRequestServiceDtos) {
//            if (memberRepository.existsByEmail(dto.getEmail())) {
//                throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
//            }
//            members.add(dto.toEntity());
//        }
//        memberRepository.saveAll(members);
//    }

    /**
     * 단일 멤버를 삭제하는 함수
     * @param id 삭제할 Member(Object)의 UID(Firebase uid)
     * @return 삭제된 Member(Object)의 UID(Firebase uid), 해당 데이터 없을 시 IllegalArgumentException 예외 처리
     */
    @Transactional
    public String delete(String id){
        Member targetMember =  memberRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException(""));

        memberRepository.delete(targetMember);

        return id;
    }

    /**
     * 다수의 멤버를 삭제하는 함수
     * @param ids 삭제할 Member(Object)의 ids(Array type)
     * @return 삭제된 Member(Object)의 ids(Array type)
     */
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

    /**
     * 다수 멤버의 삭제를 일괄 처리하기 위한 함수
     * @param batchDto 다수의 삭제할 Member(Object)의 id를 담고 있는 dtos(Array type)
     */
    private void batchDelete(Set<String> batchDto){
        if(memberRepository.findAllByIdIn(batchDto).size() != batchDto.size())
            throw new IllegalArgumentException("찾을 수 없는 ID가 들어있습니다.");

        memberRepository.deleteAllByIdInQuery(batchDto);
        batchDto.clear();
    }

    /**
     * 단일 멤버를 수정하는 함수
     * @param id 업데이트 대상 Member(Object)의 UID(Firebase uid)
     * @param memberUpdateRequestServiceDto Member(Object)의 수정 내용을 담은 dto
     * @return 수정된 Member(Object)의 내용을 담은 dto, 해당 데이터가 없을 시 INVALID_MEMBER_ID 예외 처리
     */
    @Transactional
    public MemberResponseServiceDto update(String id, MemberUpdateRequestServiceDto memberUpdateRequestServiceDto){

        Member targetMember = memberRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_ID));

        targetMember.update(memberUpdateRequestServiceDto.toEntity());

        memberRepository.save(targetMember);

        return new MemberResponseServiceDto(targetMember);
    }

//    @Transactional
//    public void updateAll(List<MemberBulkUpdateRequestServiceDto> memberBulkUpdateRequestServiceDtos){
//        List<Member> members = new ArrayList<>();
//        for(MemberBulkUpdateRequestServiceDto dto : memberBulkUpdateRequestServiceDtos) {
//
//            Member targetMember = memberRepository.findMemberById(dto.getId())
//                    .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_ID));
//
//            targetMember.update(dto.toEntity());
//            members.add(targetMember);
//        }
//        memberRepository.saveAll(members);
//    }

}
