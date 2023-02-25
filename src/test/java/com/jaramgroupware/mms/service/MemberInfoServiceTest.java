package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoRepository;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoSpecification;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoBulkUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoResponseServiceDto;
import com.jaramgroupware.mms.dto.memberInfo.serviceDto.MemberInfoUpdateRequestServiceDto;
import com.jaramgroupware.mms.utils.exception.CustomException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ComponentScan
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MemberInfoServiceTest {

    @InjectMocks
    private MemberInfoService memberInfoService;

    @Mock
    private MemberInfoRepository memberInfoRepository;

    private final TestUtils testUtils = new TestUtils();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByMember() {
        //given
        MemberInfo testGoal = testUtils.getTestMemberInfo();
        Member testMember = testUtils.getTestMember();

        doReturn(Optional.of(testGoal)).when(memberInfoRepository).findMemberInfoByMember(testMember);

        //when
        MemberInfoResponseServiceDto result = memberInfoService.findByMember(testMember);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.toString(), Objects.requireNonNull(result).toString());
        verify(memberInfoRepository).findMemberInfoByMember(testMember);
    }

    @Test
    void findAll() {
        //given
        List<MemberInfo> testList = new ArrayList<MemberInfo>();

        MemberInfo testEntity1 = testUtils.getTestMemberInfo();
        testList.add(testEntity1);

        MemberInfo testEntity2 = testUtils.getTestMemberInfo2();
        testList.add(testEntity2);

        doReturn(Optional.of(testList)).when(memberInfoRepository).findAllBy();

        //when
        List<MemberInfoResponseServiceDto> results = memberInfoService.findAll();

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(MemberInfoResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(memberInfoRepository).findAllBy();
    }

    @Test
    void findAllSpec() {
        //given
        List<MemberInfo> testList = new ArrayList<MemberInfo>();

        MemberInfo testEntity1 = testUtils.getTestMemberInfo();
        testList.add(testEntity1);

        MemberInfo testEntity2 = testUtils.getTestMemberInfo2();
        testList.add(testEntity2);

        Specification<MemberInfo> testSpec = Mockito.mock(MemberInfoSpecification.class);

        doReturn(testList).when(memberInfoRepository).findAll(testSpec);

        //when
        List<MemberInfoResponseServiceDto> results = memberInfoService.findAll(testSpec);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(MemberInfoResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(memberInfoRepository).findAll(testSpec);
    }

//    @Test
//    void findAllSpecAndPage() {
//        //given
//        List<MemberInfo> testList = new ArrayList<MemberInfo>();
//
//        MemberInfo testEntity1 = testUtils.getTestMemberInfo();
//        testList.add(testEntity1);
//
//        MemberInfo testEntity2 = testUtils.getTestMemberInfo2();
//        testList.add(testEntity2);
//
//        Specification<MemberInfo> testSpec = Mockito.mock(MemberInfoSpecification.class);
//        Pageable testPage = Mockito.mock(Pageable.class);
//        Page<MemberInfo> res = new PageImpl<>(testList);
//
//        doReturn(res).when(memberInfoRepository).findAll(testSpec,testPage);
//
//        //when
//        List<MemberInfoResponseServiceDto> results = memberInfoService.findAll(testSpec,testPage);
//
//        //then
//        Assertions.assertNotNull(results);
//        Assertions.assertEquals(testList.stream().map(MemberInfoResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
//        verify(memberInfoRepository).findAll(testSpec,testPage);
//    }

    @Test
    void add() {
        //given
        MemberInfoAddRequestServiceDto testServiceDto = MemberInfoAddRequestServiceDto
                .builder()
                .id(testUtils.getTestMemberInfo().getId())
                .phoneNumber(testUtils.getTestMemberInfo().getPhoneNumber())
                .studentID(testUtils.getTestMemberInfo().getStudentID())
                .year(testUtils.getTestMemberInfo().getYear())
                .rank(testUtils.getTestMemberInfo().getRank())
                .major(testUtils.getTestMemberInfo().getMajor())
                .dateOfBirth(testUtils.getTestMemberInfo().getDateOfBirth())
                .build();

        MemberInfo testEntity = testServiceDto.toEntity();
        testEntity.setId(testUtils.getTestMemberInfo().getId());

        doReturn(testEntity).when(memberInfoRepository).save(any());

        //when
        Integer resultID = memberInfoService.add(testServiceDto,testUtils.testUid);

        //then
        Assertions.assertNotNull(resultID);
        Assertions.assertEquals(resultID, Objects.requireNonNull(resultID));
        verify(memberInfoRepository).save(any());
    }

    @Test
    void addAll() {
        //given
        MemberInfoAddRequestServiceDto testServiceDto = MemberInfoAddRequestServiceDto
                .builder()
                .id(testUtils.getTestMemberInfo().getId())
                .phoneNumber(testUtils.getTestMemberInfo().getPhoneNumber())
                .studentID(testUtils.getTestMemberInfo().getStudentID())
                .year(testUtils.getTestMemberInfo().getYear())
                .rank(testUtils.getTestMemberInfo().getRank())
                .major(testUtils.getTestMemberInfo().getMajor())
                .dateOfBirth(testUtils.getTestMemberInfo().getDateOfBirth())
                .build();

        MemberInfoAddRequestServiceDto testServiceDto2 = MemberInfoAddRequestServiceDto
                .builder()
                .id(testUtils.getTestMemberInfo2().getId())
                .phoneNumber(testUtils.getTestMemberInfo2().getPhoneNumber())
                .studentID(testUtils.getTestMemberInfo2().getStudentID())
                .year(testUtils.getTestMemberInfo2().getYear())
                .rank(testUtils.getTestMemberInfo2().getRank())
                .major(testUtils.getTestMemberInfo2().getMajor())
                .dateOfBirth(testUtils.getTestMemberInfo2().getDateOfBirth())
                .build();

        MemberInfo testEntity = testServiceDto.toEntity();
        testEntity.setId(3);

        MemberInfo testEntity2 = testServiceDto.toEntity();
        testEntity2.setId(4);

        //when
        memberInfoService.addAll(Arrays.asList(testServiceDto,testServiceDto2),testUtils.testUid);

        //then
        verify(memberInfoRepository).saveAll(anyList());
    }

    @Test
    void delete() {
        //given
        Integer testID = testUtils.getTestMemberInfo().getId();
        Member testMember = testUtils.getTestMember();
        MemberInfo testEntity = testUtils.getTestMemberInfo();
        doReturn(Optional.of(testEntity)).when(memberInfoRepository).findMemberInfoByMember(testMember);

        //when
        Integer resultID = memberInfoService.delete(testMember);

        //then
        Assertions.assertNotNull(resultID);
        Assertions.assertEquals(testID, Objects.requireNonNull(resultID));
        verify(memberInfoRepository).findMemberInfoByMember(testMember);
        verify(memberInfoRepository).delete(any());
    }

    @Test
    void deleteAll() {
        //given
        Set<Integer> ids = new HashSet<>();

        Integer testID = testUtils.getTestMemberInfo().getId();
        ids.add(testID);

        Integer testID2 = testUtils.getTestMemberInfo2().getId();
        ids.add(testID2);

        MemberInfo testEntity = testUtils.getTestMemberInfo();
        MemberInfo testEntity2 = testUtils.getTestMemberInfo2();

        doReturn(Arrays.asList(testEntity, testEntity2)).when(memberInfoRepository).findAllByIdIn(ids);

        //when
        memberInfoService.delete(ids);

        //then
        verify(memberInfoRepository).findAllByIdIn(anySet());
        verify(memberInfoRepository).deleteAllByIdInQuery(anySet());
    }

    @Test
    void update() {
        //given
        Integer testID = testUtils.getTestMemberInfo().getId();
        Member testMember = testUtils.getTestMember();
        MemberInfoUpdateRequestServiceDto testDto = MemberInfoUpdateRequestServiceDto.builder()
                .phoneNumber("01011111111")
                .major(testUtils.getTestMemberInfo().getMajor())
                .rank(testUtils.getTestMemberInfo().getRank())
                .year(testUtils.getTestMemberInfo().getYear())
                .studentID(testUtils.getTestMemberInfo().getStudentID())
                .dateOfBirth(testUtils.getTestMemberInfo().getDateOfBirth())
                .build();

        MemberInfo targetEntity = testUtils.getTestMemberInfo();

        doReturn(Optional.of(targetEntity)).when(memberInfoRepository).findMemberInfoByMember(testMember);

        //when
        MemberInfoResponseServiceDto result = memberInfoService.update(targetEntity.getMember(), testDto, testUtils.getTestUid());

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(testDto.getYear(), Objects.requireNonNull(result).getYear());
        Assertions.assertEquals(testDto.getRank().getId(), Objects.requireNonNull(result).getRankID());
        Assertions.assertEquals(testDto.getMajor().getId(), Objects.requireNonNull(result).getMajorID());
        Assertions.assertEquals(testDto.getStudentID(), Objects.requireNonNull(result).getStudentID());
        Assertions.assertEquals(testDto.getPhoneNumber(), Objects.requireNonNull(result).getPhoneNumber());
        verify(memberInfoRepository).findMemberInfoByMember(testMember);
        verify(memberInfoRepository).save(any());
    }

//    @Test
//    void updateAll() {
//        //given
//        MemberInfo testEntity = testUtils.getTestMemberInfo();
//        MemberInfo testEntity2 = testUtils.getTestMemberInfo2();
//
//        MemberInfoBulkUpdateRequestServiceDto testDto = MemberInfoBulkUpdateRequestServiceDto.builder()
//                .id(testEntity.getId())
//                .year(testUtils.getTestMemberInfo().getYear())
//                .rank(testUtils.getTestMemberInfo().getRank())
//                .major(testUtils.getTestMemberInfo().getMajor())
//                .studentID(testUtils.getTestMemberInfo().getStudentID())
//                .phoneNumber("01011111111")
//                .dateOfBirth(testUtils.getTestDate())
//                .build();
//
//        MemberInfoBulkUpdateRequestServiceDto testDto2 = MemberInfoBulkUpdateRequestServiceDto.builder()
//                .id(testEntity2.getId())
//                .year(testUtils.getTestMemberInfo2().getYear())
//                .rank(testUtils.getTestMemberInfo2().getRank())
//                .major(testUtils.getTestMemberInfo2().getMajor())
//                .studentID(testUtils.getTestMemberInfo2().getStudentID())
//                .phoneNumber("01022222222")
//                .dateOfBirth(testUtils.getTestDate2())
//                .build();
//
//        doReturn(Arrays.asList(testEntity, testEntity2)).when(memberInfoRepository).findAllByIdIn(any());
//
//        //when
//        memberInfoService.updateAll(Arrays.asList(testDto, testDto2), testUtils.getTestUid());
//
//        //then
//        verify(memberInfoRepository).findAllByIdIn(any());
//        verify(memberInfoRepository).saveAll(anyList());
//    }

    @Test
    void DuplicatedStudentIdErrorWhenAddMemberInfo() {
        //given
        MemberInfoAddRequestServiceDto testServiceDto = MemberInfoAddRequestServiceDto
                .builder()
                .id(testUtils.getTestMemberInfo().getId())
                .phoneNumber(testUtils.getTestMemberInfo().getPhoneNumber())
                .studentID(testUtils.getTestMemberInfo().getStudentID())
                .year(testUtils.getTestMemberInfo().getYear())
                .rank(testUtils.getTestMemberInfo().getRank())
                .major(testUtils.getTestMemberInfo().getMajor())
                .dateOfBirth(testUtils.getTestMemberInfo().getDateOfBirth())
                .build();

        doReturn(true).when(memberInfoRepository).existsByStudentID(testServiceDto.getStudentID());

        //when then
        assertThrows(CustomException.class, () -> {
            memberInfoService.add(testServiceDto,testUtils.testUid);
        });
    }

//    @Test
//    void DuplicatedStudentIdErrorWhenAddMemberInfoAll() {
//        //given
//        MemberInfoAddRequestServiceDto testServiceDto = MemberInfoAddRequestServiceDto
//                .builder()
//                .id(testUtils.getTestMemberInfo().getId())
//                .phoneNumber(testUtils.getTestMemberInfo().getPhoneNumber())
//                .studentID(testUtils.getTestMemberInfo().getStudentID())
//                .year(testUtils.getTestMemberInfo().getYear())
//                .rank(testUtils.getTestMemberInfo().getRank())
//                .major(testUtils.getTestMemberInfo().getMajor())
//                .dateOfBirth(testUtils.getTestMemberInfo().getDateOfBirth())
//                .build();
//
//        MemberInfoAddRequestServiceDto testServiceDto2 = MemberInfoAddRequestServiceDto
//                .builder()
//                .id(testUtils.getTestMemberInfo2().getId())
//                .phoneNumber(testUtils.getTestMemberInfo2().getPhoneNumber())
//                .studentID(testUtils.getTestMemberInfo2().getStudentID())
//                .year(testUtils.getTestMemberInfo2().getYear())
//                .rank(testUtils.getTestMemberInfo2().getRank())
//                .major(testUtils.getTestMemberInfo2().getMajor())
//                .dateOfBirth(testUtils.getTestMemberInfo2().getDateOfBirth())
//                .build();
//
//        doReturn(true).when(memberInfoRepository).existsByStudentID(testServiceDto2.getStudentID());
//
//        //when then
//        assertThrows(CustomException.class, () -> {
//            memberInfoService.addAll(Arrays.asList(testServiceDto,testServiceDto2),testUtils.testUid);
//        });
//    }
}