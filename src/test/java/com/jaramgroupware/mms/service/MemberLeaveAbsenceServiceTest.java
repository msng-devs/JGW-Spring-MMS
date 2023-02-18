package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsenceRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
                .id(testUtils.getTestMemberLeaveAbsence().getId())
                .member(testUtils.getTestMemberLeaveAbsence().getMember())
                .status(testUtils.getTestMemberLeaveAbsence().isStatus())
                .expectedDateReturnSchool(testUtils.getTestMemberLeaveAbsence().getExpectedDateReturnSchool())
                .build();

        MemberLeaveAbsence testEntity = testServiceDto.toEntity();

        doReturn(testEntity).when(memberLeaveAbsenceRepository).save(any());

        //when
        String resultID = memberLeaveAbsenceService.add(testServiceDto);

        //then
        Assertions.assertNotNull(resultID);
        Assertions.assertEquals(resultID, Objects.requireNonNull(resultID));
        verify(memberLeaveAbsenceRepository).save(any());
    }

    @Test
    void findById() {
        //given
        String testID = testUtils.getTestMemberLeaveAbsence().getId();
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
    void delete() {
        //given
        String testID = testUtils.getTestMemberLeaveAbsence().getId();
        MemberLeaveAbsence testEntity = testUtils.getTestMemberLeaveAbsence();
        doReturn(Optional.of(testEntity)).when(memberLeaveAbsenceRepository).findMemberLeaveAbsenceById(testID);

        //when
        String resultID = memberLeaveAbsenceService.delete(testID);

        //then
        Assertions.assertNotNull(resultID);
        Assertions.assertEquals(testID, Objects.requireNonNull(resultID));
        verify(memberLeaveAbsenceRepository).findMemberLeaveAbsenceById(testID);
        verify(memberLeaveAbsenceRepository).delete(any());
    }

    @Test
    void update() {
        //given
        String testID = testUtils.getTestMemberLeaveAbsence().getId();
        MemberLeaveAbsenceUpdateRequestServiceDto testDto = MemberLeaveAbsenceUpdateRequestServiceDto.builder()
                .status(testUtils.getTestMemberLeaveAbsence().isStatus())
                .expectedDateReturnSchool(testUtils.getTestMemberLeaveAbsence().getExpectedDateReturnSchool())
                .build();

        MemberLeaveAbsence targetEntity = testUtils.getTestMemberLeaveAbsence();

        doReturn(Optional.of(targetEntity)).when(memberLeaveAbsenceRepository).findMemberLeaveAbsenceById(testID);

        //when
        MemberLeaveAbsenceResponseServiceDto result = memberLeaveAbsenceService.update(testID,testDto);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(testDto.isStatus(),Objects.requireNonNull(result).isStatus());
        Assertions.assertEquals(testDto.getExpectedDateReturnSchool(),Objects.requireNonNull(result).getExpectedDateReturnSchool());
        verify(memberLeaveAbsenceRepository).findMemberLeaveAbsenceById(testID);
        verify(memberLeaveAbsenceRepository).save(any());
    }

}
