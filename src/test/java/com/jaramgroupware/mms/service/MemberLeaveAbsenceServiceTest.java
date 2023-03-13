package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceResponseServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceUpdateRequestServiceDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ComponentScan
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MemberLeaveAbsenceServiceTest {

    @InjectMocks
    private MemberLeaveAbsenceService memberLeaveAbsenceService;

    @Mock
    private MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    private final TestUtils testUtils = new TestUtils();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void add() {
        //given
        MemberLeaveAbsenceAddRequestServiceDto testServiceDto = MemberLeaveAbsenceAddRequestServiceDto
                .builder()
                .member(testUtils.getTestMember())
                .status(testUtils.getTestMemberLeaveAbsence().isStatus())
                .expectedDateReturnSchool(testUtils.getTestMemberLeaveAbsence().getExpectedDateReturnSchool())
                .build();

        MemberLeaveAbsence testEntity = testServiceDto.toEntity();
        testEntity.setId(testUtils.getTestMemberLeaveAbsence().getId());

        doReturn(testEntity).when(memberLeaveAbsenceRepository).save(any());

        //when
        Integer resultID = memberLeaveAbsenceService.add(testServiceDto);

        //then
        Assertions.assertNotNull(resultID);
        Assertions.assertEquals(resultID, Objects.requireNonNull(resultID));
        verify(memberLeaveAbsenceRepository).save(any());
    }

    @Test
    void findById() {
        //given
        Integer testID = testUtils.getTestMemberLeaveAbsence().getId();
        MemberLeaveAbsence testEntity = testUtils.getTestMemberLeaveAbsence();

        doReturn(Optional.of(testEntity)).when(memberLeaveAbsenceRepository).findMemberLeaveAbsenceById(testID);

        //when
        MemberLeaveAbsenceResponseServiceDto result = memberLeaveAbsenceService.findById(testID);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.toString(), Objects.requireNonNull(result).toString());
        verify(memberLeaveAbsenceRepository).findMemberLeaveAbsenceById(testID);
    }

    @Test
    void findAll() {
        //given
        List<MemberLeaveAbsence> testList = new ArrayList<>();

        MemberLeaveAbsence testEntity1 = testUtils.getTestMemberLeaveAbsence();
        testList.add(testEntity1);

        MemberLeaveAbsence testEntity2 = testUtils.getTestMemberLeaveAbsence2();
        testList.add(testEntity2);

        doReturn(Optional.of(testList)).when(memberLeaveAbsenceRepository).findAllBy();

        //when
        List<MemberLeaveAbsenceResponseServiceDto> results = memberLeaveAbsenceService.findAll();

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(MemberLeaveAbsenceResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(memberLeaveAbsenceRepository).findAllBy();
    }

    @Test
    void findByMember() {
        //given
        Member testMember = testUtils.getTestMember();
        MemberLeaveAbsence testEntity = testUtils.getTestMemberLeaveAbsence();

        doReturn(Optional.of(testEntity)).when(memberLeaveAbsenceRepository).findMemberLeaveAbsenceByMember(testMember);

        //when
        MemberLeaveAbsenceResponseServiceDto result = memberLeaveAbsenceService.findByMember(testMember);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.toString(), Objects.requireNonNull(result).toString());
        verify(memberLeaveAbsenceRepository).findMemberLeaveAbsenceByMember(testMember);
    }

    @Test
    void delete() {
        //given
        Integer testID = 1;
        MemberLeaveAbsence testEntity = testUtils.getTestMemberLeaveAbsence();
        doReturn(Optional.of(testEntity)).when(memberLeaveAbsenceRepository).findMemberLeaveAbsenceByMember(testEntity.getMember());

        //when
        Integer resultID = memberLeaveAbsenceService.delete(testEntity.getMember());

        //then
        Assertions.assertNotNull(resultID);
        Assertions.assertEquals(testID, Objects.requireNonNull(resultID));
        verify(memberLeaveAbsenceRepository).findMemberLeaveAbsenceByMember(testEntity.getMember());
        verify(memberLeaveAbsenceRepository).delete(any());
    }

    @Test
    void deleteAll() {
        //given
        Set<Integer> ids = new HashSet<>();

        Integer testID = testUtils.getTestMemberLeaveAbsence().getId();
        ids.add(testID);

        Integer testID2 = testUtils.getTestMemberLeaveAbsence2().getId();
        ids.add(testID2);

        MemberLeaveAbsence testEntity = testUtils.getTestMemberLeaveAbsence();
        MemberLeaveAbsence testEntity2 = testUtils.getTestMemberLeaveAbsence2();

        doReturn(Arrays.asList(testEntity,testEntity2)).when(memberLeaveAbsenceRepository).findAllByIdIn(ids);
        doReturn(2).when(memberLeaveAbsenceRepository).deleteAllByIdInQuery(ids);

        //when
        memberLeaveAbsenceService.delete(ids);

        //then
        verify(memberLeaveAbsenceRepository).findAllByIdIn(anySet());
        verify(memberLeaveAbsenceRepository).deleteAllByIdInQuery(anySet());
    }

    @Test
    void update() {
        //given
        Member testMember = testUtils.getTestMemberLeaveAbsence().getMember();
        MemberLeaveAbsenceUpdateRequestServiceDto testDto = MemberLeaveAbsenceUpdateRequestServiceDto.builder()
                .status(testUtils.getTestMemberLeaveAbsence().isStatus())
                .expectedDateReturnSchool(testUtils.getTestMemberLeaveAbsence().getExpectedDateReturnSchool())
                .build();

        MemberLeaveAbsence targetEntity = testUtils.getTestMemberLeaveAbsence();

        doReturn(Optional.of(targetEntity)).when(memberLeaveAbsenceRepository).findMemberLeaveAbsenceByMember(testMember);

        //when
        MemberLeaveAbsenceResponseServiceDto result = memberLeaveAbsenceService.update(testMember,testDto);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(testDto.isStatus(),Objects.requireNonNull(result).isStatus());
        Assertions.assertEquals(testDto.getExpectedDateReturnSchool(),Objects.requireNonNull(result).getExpectedDateReturnSchool());
        verify(memberLeaveAbsenceRepository).findMemberLeaveAbsenceByMember(testMember);
        verify(memberLeaveAbsenceRepository).save(any());
    }

}
