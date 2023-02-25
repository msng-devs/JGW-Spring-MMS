package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberAddRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberBulkUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberResponseServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberUpdateRequestServiceDto;
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
public class MemberService {

    private final int batchSize = 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseServiceDto findById(String uid){

        Member targetMember = memberRepository.findMemberById(uid)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_ID));

        return new MemberResponseServiceDto(targetMember);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseServiceDto> findAll(){

        return memberRepository.findAllBy()
                .orElseThrow(()->new CustomException(ErrorCode.EMPTY_MEMBER))
                .stream()
                .map(MemberResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<MemberResponseServiceDto> findAll(Specification<Member> specification, Pageable pageable){

        return memberRepository.findAll(specification,pageable)
                .stream()
                .map(MemberResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<MemberResponseServiceDto> findAll(Specification<Member> specification){

        return memberRepository.findAll(specification)
                .stream()
                .map(MemberResponseServiceDto::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public String add(MemberAddRequestServiceDto memberAddRequestServiceDto){

        if(memberRepository.existsByEmail(memberAddRequestServiceDto.getEmail())) {
            throw  new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        Member targetMember = memberAddRequestServiceDto.toEntity();
        return memberRepository.save(targetMember).getId();
    }

    @Transactional
    public void addAll(List<MemberAddRequestServiceDto> memberAddRequestServiceDtos){

        List<Member> members = new ArrayList<>();
        for(MemberAddRequestServiceDto dto : memberAddRequestServiceDtos) {
            if (memberRepository.existsByEmail(dto.getEmail())) {
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
            }
            members.add(dto.toEntity());
        }
        memberRepository.saveAll(members);
    }

    @Transactional
    public String delete(String id){
        Member targetMember =  memberRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException(""));

        memberRepository.delete(targetMember);

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
        if(memberRepository.findAllByIdIn(batchDto).size() != batchDto.size())
            throw new IllegalArgumentException("찾을 수 없는 ID가 들어있습니다.");

        memberRepository.deleteAllByIdInQuery(batchDto);
        batchDto.clear();
    }

    @Transactional
    public MemberResponseServiceDto update(String id, MemberUpdateRequestServiceDto memberUpdateRequestServiceDto){

        Member targetMember = memberRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_ID));

        targetMember.update(memberUpdateRequestServiceDto.toEntity());

        memberRepository.save(targetMember);

        return new MemberResponseServiceDto(targetMember);
    }

    @Transactional
    public void updateAll(List<MemberBulkUpdateRequestServiceDto> memberBulkUpdateRequestServiceDtos){
        List<Member> members = new ArrayList<>();
        for(MemberBulkUpdateRequestServiceDto dto : memberBulkUpdateRequestServiceDtos) {

            Member targetMember = memberRepository.findMemberById(dto.getId())
                    .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_ID));

            targetMember.update(dto.toEntity());
            members.add(targetMember);
        }
        memberRepository.saveAll(members);
    }

}
