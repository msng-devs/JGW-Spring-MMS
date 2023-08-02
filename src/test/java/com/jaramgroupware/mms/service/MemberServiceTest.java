package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoRepository;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfoRepository;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.domain.withdrawal.Withdrawal;
import com.jaramgroupware.mms.domain.withdrawal.WithdrawalRepository;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.member.MemberRegisteredResponseDto;
import com.jaramgroupware.mms.dto.member.MemberResponseDto;
import com.jaramgroupware.mms.dto.member.MemberStat;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberEditRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberRegisterRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.registerCode.RegisterCodeResponseDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import com.jaramgroupware.mms.dto.withdrawal.WithdrawalResponseDto;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private TimeUtility timeUtility;
    @Mock
    private RegisterCodeService registerCodeService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberInfoRepository memberInfoRepository;
    @Mock
    private WithdrawalRepository withdrawalRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RankRepository rankRepository;
    @Mock
    private MajorRepository majorRepository;

    @Mock
    private PreMemberInfoRepository preMemberInfoRepository;
    @Mock
    private MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    @Test
    void deleteById() {
        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(true)
                .build();

        doReturn(Optional.of(target)).when(memberRepository).findById(target.getId());
        //when
        var result = memberService.deleteById(target.getId());

        //then
        assertEquals(target.getId(),result);
        verify(memberRepository).findById(target.getId());
        verify(memberRepository).delete(target);
    }

    @Test
    void withdrawal() {
        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(true)
                .build();

        var updatedTarget = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(false)
                .build();

        doReturn(Optional.of(target)).when(memberRepository).findById(target.getId());

        doReturn(LocalDate.of(2021,7,5)).when(timeUtility).nowDate();

        var withdrawal = Withdrawal.builder()
                .member(target)
                .withdrawalDate(LocalDate.of(2021,7,12))
                .createDate(LocalDate.of(2021,7,5))
                .build();

        var newWithdrawal = Withdrawal.builder()
                .id(1L)
                .member(target)
                .withdrawalDate(LocalDate.of(2021,7,12))
                .createDate(LocalDate.of(2021,7,5))
                .build();

        doReturn(newWithdrawal).when(withdrawalRepository).save(withdrawal);

        var exceptResult = WithdrawalResponseDto.builder()
                .withdrawalDate(LocalDate.of(2021,7,12))
                .build();
        //when
        var result = memberService.withdrawal(target.getId());

        //then
        assertEquals(exceptResult,result);
        verify(memberRepository).findById(target.getId());
        verify(memberRepository).save(updatedTarget);
        verify(withdrawalRepository).save(withdrawal);
    }

    @Test
    void cancelWithdrawal() {
        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(false)
                .build();

        doReturn(Optional.of(target)).when(memberRepository).findById(target.getId());

        var withdrawal = Withdrawal.builder()
                .id(1L)
                .member(target)
                .withdrawalDate(LocalDate.of(2021,7,12))
                .createDate(LocalDate.of(2021,7,5))
                .build();

        doReturn(Optional.of(withdrawal)).when(withdrawalRepository).findByMember(target);

        var updatedTarget = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(true)
                .build();
        //when
        memberService.cancelWithdrawal(target.getId());

        //then
        verify(memberRepository).findById(target.getId());
        verify(withdrawalRepository).findByMember(target);
        verify(memberRepository).save(updatedTarget);
        verify(withdrawalRepository).delete(withdrawal);
    }

    @Test
    void update() {
        //given
        var request = MemberUpdateRequestServiceDto.builder()
                .targetId("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .modifiedBy("test")
                .dateOfBirth(LocalDate.of(1999,12,31))
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .majorId(1L)
                .phoneNumber("010-1234-5678")
                .year(34)
                .rankId(1L)
                .roleId(2L)
                .studentID("201510302")
                .build();

        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(true)
                .build();

        doReturn(Optional.of(target)).when(memberRepository).findById(target.getId());

        var targetInfo = MemberInfo.builder()
                .member(target)
                .dateOfBirth(LocalDate.of(1999,12,31))
                .phoneNumber("010-1234-5678")
                .year(34)
                .rank(new Rank(1L,"테스트 랭크"))
                .major(new Major(3L,"테스트 학과2"))
                .studentID("201510302")
                .id(1)
                .build();

        doReturn(Optional.of(targetInfo)).when(memberInfoRepository).findByMember(target);
//        doReturn(true).when(memberRepository).existsByEmail(target.getEmail());
//        doReturn(true).when(memberInfoRepository).existsByPhoneNumber(targetInfo.getPhoneNumber());
//        doReturn(true).when(memberInfoRepository).existsByStudentID(targetInfo.getStudentID());

        doReturn(Optional.of(new Role(2L,"ROLE_USER"))).when(roleRepository).findById(2L);
        doReturn(Optional.of(new Rank(1L,"테스트 랭크"))).when(rankRepository).findById(1L);
        doReturn(Optional.of(new Major(1L,"테스트 학과"))).when(majorRepository).findById(1L);

        var updatedTarget = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(true)
                .build();
        var updatedTargetInfo = MemberInfo.builder()
                .member(target)
                .dateOfBirth(LocalDate.of(1999,12,31))
                .phoneNumber("010-1234-5678")
                .year(34)
                .rank(new Rank(1L,"테스트 랭크"))
                .major(new Major(1L,"테스트 학과"))
                .studentID("201510302")
                .id(1)
                .build();
        var exceptResult = MemberResponseDto
                .builder()
                .cellPhoneNumber(targetInfo.getPhoneNumber())
                .dateOfBirth(targetInfo.getDateOfBirth())
                .email(target.getEmail())
                .major(new MajorResponseDto(updatedTargetInfo.getMajor()))
                .name(target.getName())
                .rank(new RankResponseDto(updatedTargetInfo.getRank()))
                .role(new RoleResponseDto(updatedTarget.getRole()))
                .studentID(targetInfo.getStudentID())
                .uid(target.getId())
                .year(targetInfo.getYear())
                .build();

        //when
        var result = memberService.update(request);

        //then
        assertEquals(exceptResult,result);

        verify(memberRepository).findById(target.getId());
        verify(memberInfoRepository).findByMember(target);
        verify(roleRepository).findById(2L);
        verify(rankRepository).findById(1L);
        verify(majorRepository).findById(1L);
        verify(memberRepository).save(updatedTarget);
        verify(memberInfoRepository).save(updatedTargetInfo);

    }

    @Test
    void edit() {
        //given
        var request = MemberEditRequestServiceDto.builder()
                .targetId("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .majorId(1L)
                .phoneNumber("010-1234-5678")
                .modifiedBy("test")
                .build();

        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(true)
                .build();

        doReturn(Optional.of(target)).when(memberRepository).findById(target.getId());

        var targetInfo = MemberInfo.builder()
                .member(target)
                .dateOfBirth(LocalDate.of(1999,12,31))
                .phoneNumber("010-1234-5678")
                .year(34)
                .rank(new Rank(1L,"테스트 랭크"))
                .major(new Major(3L,"테스트 학과2"))
                .studentID("201510302")
                .id(1)
                .build();

        doReturn(Optional.of(targetInfo)).when(memberInfoRepository).findByMember(target);

        doReturn(Optional.of(new Major(1L,"테스트 학과"))).when(majorRepository).findById(1L);
        var updatedTarget = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(true)
                .build();
        var updatedTargetInfo = MemberInfo.builder()
                .member(target)
                .dateOfBirth(LocalDate.of(1999,12,31))
                .phoneNumber("010-1234-5678")
                .year(34)
                .rank(new Rank(1L,"테스트 랭크"))
                .major(new Major(1L,"테스트 학과"))
                .studentID("201510302")
                .id(1)
                .build();
        var exceptResult = MemberResponseDto
                .builder()
                .cellPhoneNumber(targetInfo.getPhoneNumber())
                .dateOfBirth(targetInfo.getDateOfBirth())
                .email(target.getEmail())
                .major(new MajorResponseDto(updatedTargetInfo.getMajor()))
                .name(target.getName())
                .rank(new RankResponseDto(updatedTargetInfo.getRank()))
                .role(new RoleResponseDto(updatedTarget.getRole()))
                .studentID(targetInfo.getStudentID())
                .uid(target.getId())
                .year(targetInfo.getYear())
                .build();
        //when
        var result = memberService.edit(request);
        //then
        assertEquals(exceptResult,result);
        verify(memberRepository).findById(target.getId());
        verify(memberInfoRepository).findByMember(target);
        verify(majorRepository).findById(1L);
        verify(memberRepository).save(updatedTarget);
        verify(memberInfoRepository).save(updatedTargetInfo);

    }

    @Test
    void getStatusById() {
        //given

        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(false)
                .build();

        doReturn(Optional.of(target)).when(memberRepository).findById(target.getId());

        var targetInfo = MemberInfo.builder()
                .member(target)
                .dateOfBirth(LocalDate.of(1999,12,31))
                .phoneNumber("010-1234-5678")
                .year(34)
                .rank(new Rank(1L,"테스트 랭크"))
                .major(new Major(3L,"테스트 학과2"))
                .studentID("201510302")
                .id(1)
                .build();

        doReturn(Optional.of(targetInfo)).when(memberInfoRepository).findByMember(target);

        var targetWithdrawal = Withdrawal.builder()
                .id(1L)
                .member(target)
                .withdrawalDate(LocalDate.of(2021,7,12))
                .createDate(LocalDate.of(2021,7,5))
                .build();

        doReturn(Optional.of(targetWithdrawal)).when(withdrawalRepository).findByMember(target);

        //when
        var result = memberService.getStatusById(target.getId());
        //then
        assertEquals(MemberStat.IN_WITHDRAWAL.getStatus(),result.getStatus());

    }

    @Test
    void getStatusById2() {
        //given

        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name("나수습")
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .role(new Role(2L,"ROLE_USER"))
                .status(true)
                .build();

        doReturn(Optional.of(target)).when(memberRepository).findById(target.getId());

        var targetInfo = MemberInfo.builder()
                .member(target)
                .dateOfBirth(LocalDate.of(1999,12,31))
                .phoneNumber("010-1234-5678")
                .year(34)
                .rank(new Rank(1L,"테스트 랭크"))
                .major(new Major(3L,"테스트 학과2"))
                .studentID("201510302")
                .id(1)
                .build();

        doReturn(Optional.of(targetInfo)).when(memberInfoRepository).findByMember(target);

        doReturn(Optional.empty()).when(withdrawalRepository).findByMember(target);

        //when
        var result = memberService.getStatusById(target.getId());
        //then
        assertEquals(MemberStat.ACTIVATED.getStatus(),result.getStatus());

    }
    @Test
    void registerMember() {
        //given
        var request = MemberRegisterRequestServiceDto.builder()
                .code("40f05bb1-0047-4939-b5f9-4b8b9d7205f7")
                .dateOfBirth(LocalDate.of(1999,12,31))
                .email("test@test.com")
                .phoneNumber("010-1234-5678")
                .uid("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .build();

        var preMemberInfo = PreMemberInfo
                .builder()
                .id(1L)
                .registerCode(null)
                .studentId("201510302")
                .expectedDateReturnSchool(null)
                .major(new Major(1L,"테스트 학과"))
                .rank(new Rank(1L,"테스트 랭크"))
                .year(34)
                .name("김개발")
                .role(new Role(1L,"ROLE_GUEST"))
                .build();

        var registerCode = RegisterCode.builder()
                .expiredAt(LocalDate.of(2021,7,12))
                .preMemberInfo(preMemberInfo)
                .createBy("test")
                .code("40f05bb1-0047-4939-b5f9-4b8b9d7205f7")
                .build();

        preMemberInfo.setRegisterCode(registerCode);

        var registerCodeResponse = RegisterCodeResponseDto.builder()
                .preMemberInfoId(1L)
                .code("40f05bb1-0047-4939-b5f9-4b8b9d7205f7")
                .build();

        doReturn(registerCodeResponse).when(registerCodeService).readRegisterCode(registerCodeResponse.getCode());
        doReturn(Optional.of(preMemberInfo)).when(preMemberInfoRepository).findById(registerCodeResponse.getPreMemberInfoId());

        var newMember = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .name(preMemberInfo.getName())
                .email(request.getEmail())
                .role(preMemberInfo.getRole())
                .status(true)
                .build();

        doReturn(newMember).when(memberRepository).saveAndFlush(newMember);

        var targetMemberInfo = MemberInfo.builder()
                .member(newMember)
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .year(preMemberInfo.getYear())
                .rank(preMemberInfo.getRank())
                .major(preMemberInfo.getMajor())
                .studentID(preMemberInfo.getStudentId())
                .build();

        var newMemberInfo = MemberInfo.builder()
                .id(1)
                .member(newMember)
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .year(preMemberInfo.getYear())
                .rank(preMemberInfo.getRank())
                .major(preMemberInfo.getMajor())
                .studentID(preMemberInfo.getStudentId())
                .build();

        doReturn(newMemberInfo).when(memberInfoRepository).saveAndFlush(targetMemberInfo);

        var targetLeaveAbsence = MemberLeaveAbsence.builder()
                .member(newMember)
                .status(false)
                .expectedDateReturnSchool(null)
                .build();

        var newLeaveAbsence = MemberLeaveAbsence.builder()
                .id(1)
                .member(newMember)
                .status(false)
                .expectedDateReturnSchool(null)
                .build();

        doReturn(newLeaveAbsence).when(memberLeaveAbsenceRepository).saveAndFlush(targetLeaveAbsence);

        var exceptResult = MemberRegisteredResponseDto.builder()
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .major(new MajorResponseDto(preMemberInfo.getMajor()))
                .name(preMemberInfo.getName())
                .rank(new RankResponseDto(preMemberInfo.getRank()))
                .role(new RoleResponseDto(preMemberInfo.getRole()))
                .studentID(preMemberInfo.getStudentId())
                .uid(newMember.getId())
                .year(preMemberInfo.getYear())
                .cellPhoneNumber(request.getPhoneNumber())
                .build();

        //when
        var result = memberService.registerMember(request);

        //then
        assertEquals(exceptResult,result);
        verify(registerCodeService).readRegisterCode(registerCodeResponse.getCode());
        verify(preMemberInfoRepository).findById(registerCodeResponse.getPreMemberInfoId());
        verify(memberRepository).saveAndFlush(newMember);
        verify(memberInfoRepository).saveAndFlush(targetMemberInfo);
        verify(memberLeaveAbsenceRepository).saveAndFlush(targetLeaveAbsence);
    }

}