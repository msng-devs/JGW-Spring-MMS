package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.MemberLeaveAbsenceResponseDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceUpdateRequestServiceDto;
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
class LeaveAbsenceServiceTest {

    @Mock
    private MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LeaveAbsenceService leaveAbsenceService;

    @Test
    void findByMember() {
        //given
        var expectedMember = Member
                .builder()
                .id("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .name("나수습")
                .email("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com")
                .role(Role.builder().id(2L).build())
                .status(true)
                .build();

        doReturn(Optional.of(expectedMember)).when(memberRepository).findMemberById(expectedMember.getId());

        var expectedLeaveAbsence = MemberLeaveAbsence
                .builder()
                .id(1)
                .expectedDateReturnSchool(LocalDate.of(2021, 12, 31))
                .status(true)
                .member(expectedMember)
                .build();

        doReturn(Optional.of(expectedLeaveAbsence)).when(memberLeaveAbsenceRepository).findByMember(expectedMember);

        var expected = MemberLeaveAbsenceResponseDto.builder()
                .expectedDateReturnSchool(expectedLeaveAbsence.getExpectedDateReturnSchool())
                .id(expectedLeaveAbsence.getId())
                .status(expectedLeaveAbsence.isStatus())
                .uid(expectedLeaveAbsence.getMember().getId())
                .build();
        //when
        var result = leaveAbsenceService.findByMember(expectedMember.getId());

        //then
        assertEquals(expected, result);
        verify(memberRepository).findMemberById(expectedMember.getId());
        verify(memberLeaveAbsenceRepository).findByMember(expectedMember);

    }

    @Test
    void update() {
        //given
        var request = MemberLeaveAbsenceUpdateRequestServiceDto
                .builder()
                .uid("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .expectedReturnDate(null)
                .isLeaveAbsence(false)
                .build();

        var expectedMember = Member
                .builder()
                .id("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .name("나수습")
                .email("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com")
                .role(Role.builder().id(2L).build())
                .status(true)
                .build();

        doReturn(Optional.of(expectedMember)).when(memberRepository).findMemberById(expectedMember.getId());

        var expectedLeaveAbsence = MemberLeaveAbsence
                .builder()
                .id(1)
                .expectedDateReturnSchool(LocalDate.of(2021, 12, 31))
                .status(true)
                .member(expectedMember)
                .build();

        doReturn(Optional.of(expectedLeaveAbsence)).when(memberLeaveAbsenceRepository).findByMember(expectedMember);

        var updatedExpectedLeaveAbsence = MemberLeaveAbsence
                .builder()
                .id(1)
                .expectedDateReturnSchool(null)
                .status(false)
                .member(expectedMember)
                .build();

        doReturn(updatedExpectedLeaveAbsence).when(memberLeaveAbsenceRepository).save(updatedExpectedLeaveAbsence);

        var expected = MemberLeaveAbsenceResponseDto
                .builder()
                .id(expectedLeaveAbsence.getId())
                .uid(expectedMember.getId())
                .status(false)
                .expectedDateReturnSchool(null)
                .build();
        //when
        var result = leaveAbsenceService.update(request);

        //then
        assertEquals(expected, result);
        verify(memberRepository).findMemberById(expectedMember.getId());
        verify(memberLeaveAbsenceRepository).findByMember(expectedMember);
        verify(memberLeaveAbsenceRepository).save(updatedExpectedLeaveAbsence);
    }
}