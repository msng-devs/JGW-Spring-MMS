package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoRepository;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.domain.withdrawal.Withdrawal;
import com.jaramgroupware.mms.domain.withdrawal.WithdrawalRepository;
import com.jaramgroupware.mms.dto.member.MemberResponseDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberEditRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import com.jaramgroupware.mms.dto.withdrawal.WithdrawalResponseDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 멤버에 관한 비즈니스 로직이 들어있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final WithdrawalRepository withdrawalRepository;

    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public String deleteById(String id){
        var targetMember =  memberRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 멤버입니다."));

        memberRepository.delete(targetMember);

        return id;
    }

    @Transactional
    public WithdrawalResponseDto withdraw(String id){
        var targetMember =  memberRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 멤버입니다."));

        targetMember.setStatus(false);
        memberRepository.save(targetMember);

        var withdrawal = new Withdrawal(targetMember,7);
        withdrawalRepository.save(withdrawal);

        return new WithdrawalResponseDto(withdrawal);
    }

    @Transactional
    public MemberResponseDto update(MemberUpdateRequestServiceDto requestDto){

        var targetMember = memberRepository.findById(requestDto.getTargetId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 멤버입니다."));
        var targetMemberInfo = memberInfoRepository.findByMember(targetMember)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"회원 정보가 존재하지 않습니다."));

        //중복 체크
        isExistsEmail(requestDto.getEmail());
        isExistsPhone(requestDto.getPhoneNumber());
        isExistsStudentId(requestDto.getStudentID());

        var targetRole = roleRepository.findRoleById(requestDto.getRoleId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 Role입니다."));
        var targetRank = rankRepository.findById(requestDto.getRankId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 Rank입니다."));
        var targetMajor = majorRepository.findById(requestDto.getMajorId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 Major입니다."));

        targetMember.update(requestDto.toMemberEntity(targetRole));
        targetMemberInfo.update(requestDto.toMemberInfoEntity(targetMember,targetMajor,targetRank));

        memberRepository.save(targetMember);
        memberInfoRepository.save(targetMemberInfo);

        return new MemberResponseDto(targetMember,targetMemberInfo);
    }

    @Transactional
    public MemberResponseDto edit(MemberEditRequestServiceDto requestDto){
        var targetMember = memberRepository.findById(requestDto.getTargetId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 멤버입니다."));
        var targetMemberInfo = memberInfoRepository.findByMember(targetMember)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"회원 정보가 존재하지 않습니다."));

        //중복 체크
        isExistsEmail(requestDto.getEmail());
        isExistsPhone(requestDto.getPhoneNumber());

        var targetMajor = majorRepository.findById(requestDto.getMajorId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"존재하지 않는 Major입니다."));

        targetMember.update(requestDto.toMemberEntity());
        targetMemberInfo.update(requestDto.toMemberInfoEntity(targetMember,targetMajor));

        memberRepository.save(targetMember);
        memberInfoRepository.save(targetMemberInfo);

        return new MemberResponseDto(targetMember,targetMemberInfo);
    }

    @Transactional(readOnly = true)
    public void isExistsEmail(String email){
        if(memberRepository.existsByEmail(email)) throw new ServiceException(ServiceErrorCode.ALREADY_EXISTS,"이미 존재하는 이메일입니다.");
    }
    @Transactional(readOnly = true)
    public void isExistsPhone(String phone){
        if(memberInfoRepository.existsByPhoneNumber(phone)) throw new ServiceException(ServiceErrorCode.ALREADY_EXISTS,"이미 존재하는 전화번호입니다.");
    }
    @Transactional(readOnly = true)
    public void isExistsStudentId(String studentId){
         if(memberInfoRepository.existsByStudentID(studentId)) throw new ServiceException(ServiceErrorCode.ALREADY_EXISTS,"이미 존재하는 학번입니다.");
    }

}
