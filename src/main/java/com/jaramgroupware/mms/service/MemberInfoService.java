package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoRepository;
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

    @Transactional(readOnly = true)
    public MemberInfoResponseServiceDto findByMember(Member member){

        MemberInfo targetMemberInfo = memberInfoRepository.findMemberInfoByMember(member)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_INFO));

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

    @Transactional
    public Integer delete(Member member){
        MemberInfo targetMemberInfo = memberInfoRepository.findMemberInfoByMember(member)
                .orElseThrow(()->new IllegalArgumentException(""));

        memberInfoRepository.delete(targetMemberInfo);

        return targetMemberInfo.getId();
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
        if(memberInfoRepository.findAllByIdIn(batchDto).size() != batchDto.size())
            throw new IllegalArgumentException("찾을 수 없는 ID가 들어있습니다.");

        memberInfoRepository.deleteAllByIdInQuery(batchDto);
        batchDto.clear();
    }

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