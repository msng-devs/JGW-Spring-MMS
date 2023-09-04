package com.jaramgroupware.mms.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoRepository;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfoRepository;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.domain.withdrawal.Withdrawal;
import com.jaramgroupware.mms.domain.withdrawal.WithdrawalRepository;
import com.jaramgroupware.mms.dto.member.*;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberEditRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberRegisterRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberUpdateRequestServiceDto;

import com.jaramgroupware.mms.dto.withdrawal.WithdrawalResponseDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import com.jaramgroupware.mms.utils.mail.MailSendRequest;
import com.jaramgroupware.mms.utils.mail.MailStormClient;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 멤버에 관한 비즈니스 로직이 들어있는 클래스
 *
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 * @since 2023-03-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final TimeUtility timeUtility;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final MailStormClient mailStormClient;
    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;
    private final MajorRepository majorRepository;
    private final RegisterCodeService registerCodeService;
    private final PreMemberInfoRepository preMemberInfoRepository;

    private final MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    @Transactional
    public String deleteById(String id) {
        var targetMember = memberRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 멤버입니다."));

        memberRepository.delete(targetMember);

        return id;
    }

    @Transactional
    public WithdrawalResponseDto withdrawal(String id) {
        var targetMember = memberRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 멤버입니다."));

        targetMember.setStatus(false);
        memberRepository.save(targetMember);

        var now = timeUtility.nowDate();

        var withdrawal = Withdrawal.builder()
                .member(targetMember)
                .createDate(now)
                .withdrawalDate(now.plusDays(7))
                .build();

        var newWithdrawal = withdrawalRepository.save(withdrawal);

        return new WithdrawalResponseDto(newWithdrawal);
    }

    @Transactional
    public void cancelWithdrawal(String id) {
        var targetMember = memberRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 멤버입니다."));
        var targetWithdrawal = withdrawalRepository.findByMember(targetMember)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "회원 탈퇴 신청 정보가 없습니다."));

        targetMember.setStatus(true);
        memberRepository.save(targetMember);

        withdrawalRepository.delete(targetWithdrawal);
    }

    @Transactional
    public MemberResponseDto update(MemberUpdateRequestServiceDto requestDto) {

        var targetMember = memberRepository.findById(requestDto.getTargetId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 멤버입니다."));
        var targetMemberInfo = memberInfoRepository.findByMember(targetMember)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "회원 정보가 존재하지 않습니다."));

        //중복 체크
        if (!requestDto.getEmail().equals(targetMember.getEmail())) isExistsEmail(requestDto.getEmail());
        if (!requestDto.getPhoneNumber().equals(targetMemberInfo.getPhoneNumber()))
            isExistsPhone(requestDto.getPhoneNumber());
        if (!requestDto.getStudentID().equals(targetMemberInfo.getStudentID()))
            isExistsStudentId(requestDto.getStudentID());

        var targetRole = roleRepository.findById(requestDto.getRoleId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Role입니다."));
        var targetRank = rankRepository.findById(requestDto.getRankId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Rank입니다."));
        var targetMajor = majorRepository.findById(requestDto.getMajorId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Major입니다."));

        targetMember.update(requestDto.toMemberEntity(targetRole));
        targetMemberInfo.update(requestDto.toMemberInfoEntity(targetMember, targetMajor, targetRank, timeUtility.nowDateTime()));

        memberRepository.save(targetMember);
        memberInfoRepository.save(targetMemberInfo);

        return new MemberResponseDto(targetMember, targetMemberInfo);
    }

    @Transactional
    public MemberResponseDto edit(MemberEditRequestServiceDto requestDto) {

        var targetMember = memberRepository.findById(requestDto.getTargetId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 멤버입니다."));
        var targetMemberInfo = memberInfoRepository.findByMember(targetMember)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "회원 정보가 존재하지 않습니다."));

        //중복 체크
        if (!requestDto.getPhoneNumber().equals(targetMemberInfo.getPhoneNumber()))
            isExistsPhone(requestDto.getPhoneNumber());

        var targetMajor = majorRepository.findById(requestDto.getMajorId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Major입니다."));

        targetMember.update(requestDto.toMemberEntity());
        targetMemberInfo.update(requestDto.toMemberInfoEntity(targetMember, targetMajor, timeUtility.nowDateTime()));

        memberRepository.save(targetMember);
        memberInfoRepository.save(targetMemberInfo);

        return new MemberResponseDto(targetMember, targetMemberInfo);
    }

    @Transactional(readOnly = true)
    public StatusResponseDto getStatusById(String id) {

        var targetMember = memberRepository.findById(id)
                .orElse(null);

        var targetMemberInfo = memberInfoRepository.findByMember(targetMember)
                .orElse(null);

        var withdrawal = withdrawalRepository.findByMember(targetMember)
                .orElse(null);

        var status = checkStatus(targetMember, targetMemberInfo, withdrawal);
        return new StatusResponseDto(status);
    }

    @Transactional
    public MemberRegisteredResponseDto registerMember(MemberRegisterRequestServiceDto dto) {
        var registerCode = registerCodeService.readRegisterCode(dto.getCode());
        var targetPreMember = preMemberInfoRepository.findById(registerCode.getPreMemberInfoId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "해당 코드에 해당하는 사전 회원 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));

        var member = dto.toMemberEntity(targetPreMember);
        var newMember = memberRepository.saveAndFlush(member);

        var memberInfo = dto.toMemberInfoEntity(targetPreMember, newMember, timeUtility.nowDateTime());
        var newMemberInfo = memberInfoRepository.saveAndFlush(memberInfo);

        var leaveAbsence = createLeaveAbsence(newMember, targetPreMember);
        var newLeaveAbsence = memberLeaveAbsenceRepository.saveAndFlush(leaveAbsence);

        preMemberInfoRepository.delete(targetPreMember);

        mailStormClient.sendWelcomeEmail(newMember.getEmail(),"[자람 그룹웨어] 회원가입을 축하드립니다! 🎉🎉🎉", Map.of("name", newMember.getName()));
        return new MemberRegisteredResponseDto(newMember, newMemberInfo, newLeaveAbsence);

    }


    private MemberStat checkStatus(Member member, MemberInfo memberInfo, Withdrawal withdrawal) {
        if (notRegistered(member, memberInfo)) {
            return MemberStat.NOT_REGISTERED;
        } else if (isWithdrawal(member, memberInfo, withdrawal)) {
            return MemberStat.IN_WITHDRAWAL;
        } else if (!member.isStatus()) {
            return MemberStat.NOT_ACTIVATED;
        } else return MemberStat.ACTIVATED;
    }

    private boolean notRegistered(Member member, MemberInfo memberInfo) {
        return memberInfo == null && member == null;
    }


    private boolean isWithdrawal(Member member, MemberInfo memberInfo, Withdrawal withdrawal) {
        return memberInfo != null && !member.isStatus() && withdrawal != null;
    }

    @Transactional(readOnly = true)
    public void isExistsEmail(String email) {
        if (memberRepository.existsByEmail(email))
            throw new ServiceException(ServiceErrorCode.ALREADY_EXISTS, "이미 존재하는 이메일입니다.");
    }

    @Transactional(readOnly = true)
    public void isExistsPhone(String phone) {
        if (memberInfoRepository.existsByPhoneNumber(phone))
            throw new ServiceException(ServiceErrorCode.ALREADY_EXISTS, "이미 존재하는 전화번호입니다.");
    }

    @Transactional(readOnly = true)
    public void isExistsStudentId(String studentId) {
        if (memberInfoRepository.existsByStudentID(studentId))
            throw new ServiceException(ServiceErrorCode.ALREADY_EXISTS, "이미 존재하는 학번입니다.");
    }

    public MemberLeaveAbsence createLeaveAbsence(Member member, PreMemberInfo preMemberInfo) {
        if (preMemberInfo.getExpectedDateReturnSchool() != null) {
            return MemberLeaveAbsence.builder()
                    .member(member)
                    .status(true)
                    .expectedDateReturnSchool(preMemberInfo.getExpectedDateReturnSchool())
                    .build();
        }
        return MemberLeaveAbsence.builder()
                .member(member)
                .status(false)
                .expectedDateReturnSchool(null)
                .build();
    }

    @Transactional
    public List<MemberDeletedResponseDto> processWithdrawal(LocalDate now) {
        var targets = withdrawalRepository.findAllExpired(now);
        if (targets.isEmpty()) return List.of();

        var targetMembers = targets.stream().map(Withdrawal::getMember).toList();

        var successCnt = 0;
        List<MemberDeletedResponseDto> results = new ArrayList<>();

        for (var member : targetMembers) {
            var result = deleteMember(member);
            if (result) {
                successCnt++;
                results.add(new MemberDeletedResponseDto(member, true));
            } else {
                results.add(new MemberDeletedResponseDto(member, false));
            }
        }

        log.info("[WithdrawalScheduler] withdrawal process : {} / {}", successCnt, targetMembers.size());
        return results;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deleteMember(Member targetMember) {

        try {
            var userRecord = FirebaseAuth.getInstance().getUser(targetMember.getId());
            memberRepository.delete(targetMember);
            FirebaseAuth.getInstance().deleteUser(userRecord.getUid());
            log.info("[WithdrawalScheduler] delete member : {} {} {}", targetMember.getId(), targetMember.getEmail(), userRecord.getUid());
        } catch (FirebaseAuthException e) {
            log.error("[WithdrawalScheduler] delete member error : {} {} {}", targetMember.getId(), targetMember.getEmail(), e.getMessage());
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public void sendDevAlert(String subject, String content) {
        var targetRole = roleRepository.findRoleById(5L)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "존재하지 않는 Role입니다."));
        var targets = memberRepository.findAllByRole(targetRole);
        targets.forEach(member -> {
            mailStormClient.sendAlertEmail(member.getEmail(), subject , Map.of("name", member.getName(), "context", content));
        });
    }


}
