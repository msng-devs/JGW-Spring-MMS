package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberView.MemberView;
import com.jaramgroupware.mms.domain.memberView.MemberViewRepository;
import com.jaramgroupware.mms.dto.memberView.MemberViewDatailResponseDto;
import com.jaramgroupware.mms.dto.memberView.MemberViewResponseDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberViewService {

    private final MemberViewRepository memberViewRepository;

    @Transactional(readOnly = true)
    public MemberViewDatailResponseDto findByIdDetail(String uid){
        var memberView = memberViewRepository.findById(uid)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"해당 uid를 가진 회원이 없습니다."));
        return new MemberViewDatailResponseDto(memberView);
    }

    @Transactional(readOnly = true)
    public MemberViewResponseDto findById(String uid){
        var memberView = memberViewRepository.findById(uid)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"해당 uid를 가진 회원이 없습니다."));
        return new MemberViewResponseDto(memberView);
    }

    @Transactional(readOnly = true)
    public Page<MemberViewDatailResponseDto> findAll(MultiValueMap<String,String> param, Pageable pageable){
        var memberViews = memberViewRepository.findAllWithQueryParams(pageable,param);
        var total = memberViewRepository.countAllWithQueryParams(param);
        return new PageImpl<>(memberViews.stream().map(MemberViewDatailResponseDto::new).toList(),pageable,total);
    }

}
