package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.withdrawal.Withdrawal;
import com.jaramgroupware.mms.domain.withdrawal.WithdrawalRepository;
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
    private final WithdrawalRepository withdrawalRepository;
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

}
