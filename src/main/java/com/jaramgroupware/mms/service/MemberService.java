package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.rank.Rank;
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
import org.springframework.dao.DataIntegrityViolationException;
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
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_TIMETABLE_ID));

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

    @Transactional(readOnly = true)
    public List<MemberResponseServiceDto> findAll(Set<Rank> ranks){

        return memberRepository.findTargetMember(ranks)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_RANK_ID))
                .stream()
                .map(MemberResponseServiceDto::new)
                .collect(Collectors.toList());

    }
    @Transactional
    public String add(MemberAddRequestServiceDto memberAddRequestServiceDto,String who){
        Member targetMember = memberAddRequestServiceDto.toEntity();
        targetMember.setCreateBy(who);
        targetMember.setModifiedBy(who);
        return memberRepository.save(targetMember).getId();
    }

    @Transactional
    public void add(List<MemberAddRequestServiceDto> memberAddRequestServiceDto,String who){
        List<MemberAddRequestServiceDto> batchDto = new ArrayList<>();
        for (MemberAddRequestServiceDto dto:memberAddRequestServiceDto) {
            batchDto.add(dto);
            if(batchDto.size() == batchSize){
                batchAdd(batchDto,who);
            }
        }
        if(!batchDto.isEmpty()) {
            batchAdd(batchDto,who);
        }
    }

    public void batchAdd(List<MemberAddRequestServiceDto> batchDto,String who){
        memberRepository.bulkInsert(batchDto.stream().map(MemberAddRequestServiceDto::toEntity).collect(Collectors.toList()),who);
        batchDto.clear();
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
            throw new IllegalArgumentException("?????? ??? ?????? ID??? ??????????????????.");

        memberRepository.deleteAllByIdInQuery(batchDto);
        batchDto.clear();
    }
    @Transactional
    public MemberResponseServiceDto update(String id, MemberUpdateRequestServiceDto memberUpdateRequestServiceDto,String who){

        Member targetMember = memberRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.INVALID_MEMBER_ID));

        targetMember.update(memberUpdateRequestServiceDto.toEntity(),who);

        memberRepository.save(targetMember);

        return new MemberResponseServiceDto(targetMember);
    }

    @Transactional
    public void update(List<MemberBulkUpdateRequestServiceDto> updateDtos, String who){
        List<MemberBulkUpdateRequestServiceDto> batchDto = new ArrayList<>();
        for (MemberBulkUpdateRequestServiceDto dto:updateDtos) {
            batchDto.add(dto);
            if(batchDto.size() == batchSize){
                batchUpdate(batchDto,who);
            }
        }
        if(!batchDto.isEmpty()) {
            batchUpdate(batchDto,who);
        }
    }

    private void batchUpdate(List<MemberBulkUpdateRequestServiceDto> batchDto,String who){
        Set<String> ids = batchDto.stream().map(MemberBulkUpdateRequestServiceDto::getId).collect(Collectors.toSet());
        if(memberRepository.findAllByIdIn(ids).size() != ids.size())
            throw new IllegalArgumentException("?????? ??? ?????? ID??? ??????????????????.");
        memberRepository.bulkUpdate(batchDto.stream().map(MemberBulkUpdateRequestServiceDto::toEntity).collect(Collectors.toList()),who);
        batchDto.clear();
        ids.clear();
    }
}
