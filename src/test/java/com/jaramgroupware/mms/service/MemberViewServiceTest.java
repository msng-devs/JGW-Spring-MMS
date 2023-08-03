package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.memberView.MemberView;
import com.jaramgroupware.mms.domain.memberView.MemberViewRepository;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.memberView.MemberViewDatailResponseDto;
import com.jaramgroupware.mms.dto.memberView.MemberViewResponseDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class MemberViewServiceTest {

    @Mock
    private MemberViewRepository memberViewRepository;

    @InjectMocks
    private MemberViewService memberViewService;

    @Test
    void findByIdDetail() {
        //given
        var target = MemberView
                .builder()
                .uid("test")
                .name("test")
                .isLeaveAbsence(false)
                .email("test")
                .majorName("test")
                .major(1L)
                .rankName("test")
                .rank(1L)
                .year(1)
                .role(1L)
                .roleName("test")
                .cellPhoneNumber("test")
                .dateOfBirth(LocalDate.of(2021,1,1))
                .status(true)
                .build();

        doReturn(Optional.of(target)).when(memberViewRepository).findById("test");

        var exceptResult = MemberViewDatailResponseDto.builder()
                .uid("test")
                .name("test")
                .isLeaveAbsence(false)
                .email("test")
                .rank(RankResponseDto.builder().id(1L).name("test").build())
                .email("test")
                .major(MajorResponseDto.builder().id(1L).name("test").build())
                .year(1)
                .role(RoleResponseDto.builder().id(1L).name("test").build())
                .cellPhoneNumber("test")
                .dateOfBirth(LocalDate.of(2021,1,1))
                .status(true)
                .build();
        //when
        var result = memberViewService.findByIdDetail("test");

        //then
        assertEquals(exceptResult,result);
        verify(memberViewRepository).findById("test");
    }

    @Test
    void findById() {
        //given
        var target = MemberView
                .builder()
                .uid("test")
                .name("test")
                .isLeaveAbsence(false)
                .email("test")
                .majorName("test")
                .major(1L)
                .rankName("test")
                .rank(1L)
                .year(1)
                .role(1L)
                .roleName("test")
                .studentId("2021000000")
                .cellPhoneNumber("test")
                .dateOfBirth(LocalDate.of(2021,1,1))
                .status(true)
                .build();

        doReturn(Optional.of(target)).when(memberViewRepository).findById("test");

        var exceptResult = MemberViewResponseDto.builder()
                .uid("test")
                .name("test")
                .email("test")
                .studentId("21")
                .major(MajorResponseDto.builder().id(1L).name("test").build())
                .rank(RankResponseDto.builder().id(1L).name("test").build())
                .year(1)
                .build();
        //when
        var result = memberViewService.findById("test");

        //then
        assertEquals(exceptResult,result);
        verify(memberViewRepository).findById("test");

    }

    @Test
    void findAll() {
        //given
        var target = MemberView
                .builder()
                .uid("test")
                .name("test")
                .isLeaveAbsence(false)
                .email("test")
                .majorName("test")
                .major(1L)
                .rankName("test")
                .rank(1L)
                .year(1)
                .role(1L)
                .roleName("test")
                .cellPhoneNumber("test")
                .dateOfBirth(LocalDate.of(2021,1,1))
                .status(true)
                .build();

        var target2 = MemberView
                .builder()
                .uid("test2")
                .name("test")
                .isLeaveAbsence(false)
                .email("test")
                .majorName("test")
                .major(1L)
                .rankName("test")
                .rank(1L)
                .year(1)
                .role(1L)
                .roleName("test")
                .cellPhoneNumber("test")
                .dateOfBirth(LocalDate.of(2021,1,1))
                .status(true)
                .build();

        var targetList = Arrays.asList(target,target2);



        var pageable = Mockito.mock(Pageable.class);
        var params = new LinkedMultiValueMap<String,String>();

        doReturn(targetList).when(memberViewRepository).findAllWithQueryParams(pageable,params);

        var exceptDto = MemberViewDatailResponseDto.builder()
                .uid("test")
                .name("test")
                .isLeaveAbsence(false)
                .email("test")
                .rank(RankResponseDto.builder().id(1L).name("test").build())
                .email("test")
                .major(MajorResponseDto.builder().id(1L).name("test").build())
                .year(1)
                .role(RoleResponseDto.builder().id(1L).name("test").build())
                .cellPhoneNumber("test")
                .dateOfBirth(LocalDate.of(2021,1,1))
                .status(true)
                .build();

        var exceptDto2 = MemberViewDatailResponseDto.builder()
                .uid("test2")
                .name("test")
                .isLeaveAbsence(false)
                .email("test")
                .rank(RankResponseDto.builder().id(1L).name("test").build())
                .email("test")
                .major(MajorResponseDto.builder().id(1L).name("test").build())
                .year(1)
                .role(RoleResponseDto.builder().id(1L).name("test").build())
                .cellPhoneNumber("test")
                .dateOfBirth(LocalDate.of(2021,1,1))
                .status(true)
                .build();

        var exceptList = Arrays.asList(exceptDto,exceptDto2);

        //when
        var result = memberViewService.findAll(params,pageable);

        //then
        assertEquals(2,result.size());
        assertTrue(result.containsAll(exceptList));
    }
}