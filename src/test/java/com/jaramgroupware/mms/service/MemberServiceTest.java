package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.member.MemberRepository;
import com.jaramgroupware.mms.domain.member.MemberSpecification;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberAddRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberBulkUpdateRequestServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberResponseServiceDto;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberUpdateRequestServiceDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ComponentScan
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

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
        MemberAddRequestServiceDto testServiceDto = MemberAddRequestServiceDto
                .builder()
                .id(testUtils.getTestMember().getId())
                .name(testUtils.getTestMember().getName())
                .role(testUtils.getTestMember().getRole())
                .email(testUtils.getTestMember().getEmail())
                .status(testUtils.getTestMember().isStatus())
                .build();

        Member testEntity = testServiceDto.toEntity();
        testEntity.setId(testUtils.getTestMember().getId());

        doReturn(testEntity).when(memberRepository).save(any());

        //when
        String resultID = memberService.add(testServiceDto);

        //then
        Assertions.assertNotNull(resultID);
        Assertions.assertEquals(resultID, Objects.requireNonNull(resultID));
        verify(memberRepository).save(any());
    }

    @Test
    void addAll() {
        //given
        MemberAddRequestServiceDto testServiceDto = MemberAddRequestServiceDto
                .builder()
                .id(testUtils.getTestMember().getId())
                .name(testUtils.getTestMember().getName())
                .role(testUtils.getTestMember().getRole())
                .email(testUtils.getTestMember().getEmail())
                .status(testUtils.getTestMember().isStatus())
                .build();

        MemberAddRequestServiceDto testServiceDto2 = MemberAddRequestServiceDto
                .builder()
                .id(testUtils.getTestMember2().getId())
                .name(testUtils.getTestMember2().getName())
                .role(testUtils.getTestMember2().getRole())
                .email(testUtils.getTestMember2().getEmail())
                .status(testUtils.getTestMember2().isStatus())
                .build();

        Member testEntity = testServiceDto.toEntity();
        testEntity.setId(testUtils.getTestMember().getId());

        Member testEntity2 = testServiceDto.toEntity();
        testEntity2.setId(testUtils.getTestMember2().getId());

        //when
        memberService.add(Arrays.asList(testServiceDto,testServiceDto2));

        //then
        verify(memberRepository).bulkInsert(any());
    }

    @Test
    void findById() {

        //given
        String testID = testUtils.getTestMember().getId();
        Member testEntity = testUtils.getTestMember();

        doReturn(Optional.of(testEntity)).when(memberRepository).findMemberById(testID);

        //when
        MemberResponseServiceDto result = memberService.findById(testID);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.toString(), Objects.requireNonNull(result).toString());
        verify(memberRepository).findMemberById(testID);
    }

    @Test
    void findAll() {
        //given
        List<Member> testList = new ArrayList<Member>();

        Member testEntity1 = testUtils.getTestMember();
        testList.add(testEntity1);

        Member testEntity2 = testUtils.getTestMember2();
        testList.add(testEntity2);

        doReturn(Optional.of(testList)).when(memberRepository).findAllBy();

        //when
        List<MemberResponseServiceDto> results = memberService.findAll();

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(MemberResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(memberRepository).findAllBy();
    }

    @Test
    void findAllSpec() {
        //given
        List<Member> testList = new ArrayList<Member>();

        Member testEntity1 = testUtils.getTestMember();
        testList.add(testEntity1);

        Member testEntity2 = testUtils.getTestMember2();
        testList.add(testEntity2);

        Specification<Member> testSpec = Mockito.mock(MemberSpecification.class);

        doReturn(testList).when(memberRepository).findAll(testSpec);

        //when
        List<MemberResponseServiceDto> results = memberService.findAll(testSpec);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(MemberResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(memberRepository).findAll(testSpec);
    }

    @Test
    void findAllSpecAndPage() {
        //given
        List<Member> testList = new ArrayList<Member>();

        Member testEntity1 = testUtils.getTestMember();
        testList.add(testEntity1);

        Member testEntity2 = testUtils.getTestMember2();
        testList.add(testEntity2);

        Specification<Member> testSpec = Mockito.mock(MemberSpecification.class);
        Pageable testPage = Mockito.mock(Pageable.class);
        Page<Member> res = new PageImpl<>(testList);

        doReturn(res).when(memberRepository).findAll(testSpec,testPage);

        //when
        List<MemberResponseServiceDto> results = memberService.findAll(testSpec,testPage);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(MemberResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(memberRepository).findAll(testSpec,testPage);
    }

//    @Test
//    void findAllRank() {
//        //given
//        List<Member> testList = new ArrayList<Member>();
//
//        Member testEntity1 = testUtils.getTestMember();
//        testList.add(testEntity1);
//
//        Member testEntity2 = testUtils.getTestMember2();
//        testList.add(testEntity2);
//
//        Set<Rank> testRank = testList.stream().map(Member::getRank).collect(Collectors.toSet());
//
//        doReturn(Optional.of(testList)).when(memberRepository).findTargetMember(testRank);
//
//        //when
//        List<MemberResponseServiceDto> results = memberService.findAll(testRank);
//
//        //then
//        Assertions.assertNotNull(results);
//        Assertions.assertEquals(testList.stream().map(MemberResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
//        verify(memberRepository).findTargetMember(testRank);
//    }
//
    @Test
    void delete() {

        //given
        String testID = testUtils.getTestMember().getId();
        Member testEntity = testUtils.getTestMember();
        doReturn(Optional.of(testEntity)).when(memberRepository).findById(testID);

        //when
        String resultID = memberService.delete(testID);

        //then
        Assertions.assertNotNull(resultID);
        Assertions.assertEquals(testID, Objects.requireNonNull(resultID));
        verify(memberRepository).findById(testID);
        verify(memberRepository).delete(any());
    }

    @Test
    void deleteAll() {

        //given
        Set<String> ids = new HashSet<>();

        String testID = testUtils.getTestMember().getId();
        ids.add(testID);

        String testID2 = testUtils.getTestMember2().getId();
        ids.add(testID2);

        Member testEntity = testUtils.getTestMember();
        Member testEntity2 = testUtils.getTestMember2();

        doReturn(Arrays.asList(testEntity,testEntity2)).when(memberRepository).findAllByIdIn(ids);

        //when
        memberService.delete(ids);

        //then
        verify(memberRepository).findAllByIdIn(anySet());
        verify(memberRepository).deleteAllByIdInQuery(anySet());
    }

    @Test
    void update() {
        //given
        String testID = testUtils.getTestMember().getId();
        MemberUpdateRequestServiceDto testDto = MemberUpdateRequestServiceDto.builder()
                .name(testUtils.getTestMember().getName())
                .role(testUtils.getTestMember().getRole())
                .email(testUtils.getTestMember().getEmail())
                .status(testUtils.getTestMember().isStatus())
                .build();

        Member targetEntity = testUtils.getTestMember();

        doReturn(Optional.of(targetEntity)).when(memberRepository).findById(testID);

        //when
        MemberResponseServiceDto result = memberService.update(testID,testDto);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(testDto.getRole().getId(),Objects.requireNonNull(result).getRoleID());
        Assertions.assertEquals(testDto.getEmail(),Objects.requireNonNull(result).getEmail());
        Assertions.assertEquals(testDto.isStatus(),Objects.requireNonNull(result).isStatus());
        verify(memberRepository).findById(testID);
        verify(memberRepository).save(any());

    }

    @Test
    void updateAll() {

        //given
        Member testEntity = testUtils.getTestMember();
        Member testEntity2 = testUtils.getTestMember2();

        MemberBulkUpdateRequestServiceDto testDto = MemberBulkUpdateRequestServiceDto.builder()
                .id(testEntity.getId())
                .name(testUtils.getTestMember().getName())
                .role(testUtils.getTestMember().getRole())
                .email(testUtils.getTestMember().getEmail())
                .status(testUtils.getTestMember().isStatus())
                .build();

        MemberBulkUpdateRequestServiceDto testDto2 = MemberBulkUpdateRequestServiceDto.builder()
                .id(testEntity2.getId())
                .name(testUtils.getTestMember2().getName())
                .role(testUtils.getTestMember2().getRole())
                .email(testUtils.getTestMember2().getEmail())
                .status(testUtils.getTestMember2().isStatus())
                .build();

        doReturn(Arrays.asList(testEntity,testEntity2)).when(memberRepository).findAllByIdIn(any());

        //when
        memberService.update(Arrays.asList(testDto,testDto2));

        //then
        verify(memberRepository).findAllByIdIn(any());
        verify(memberRepository).bulkUpdate(anyList());
    }

    @Test
    void DuplicatedEmailErrorWhenAddMember() {
        //given
        Member testEntity = testUtils.getTestMember();
        MemberAddRequestServiceDto testDto = MemberAddRequestServiceDto.builder()
                .id(testUtils.getTestMember2().getId())
                .name(testUtils.getTestMember2().getName())
                .role(testUtils.getTestMember2().getRole())
                .email(testUtils.getTestMember().getEmail())
                .status(testUtils.getTestMember2().isStatus())
                .build();

        doReturn(true).when(memberRepository).existsByEmail(testEntity.getEmail());

        //when then
        assertThrows(CustomException.class, () -> {
            memberService.add(testDto);
        });
    }

//    @Test
//    void DuplicatedEmailErrorWhenAddMemberAll() {
//        //given
//        MemberAddRequestServiceDto testServiceDto = MemberAddRequestServiceDto
//                .builder()
//                .id(testUtils.getTestMember().getId())
//                .name(testUtils.getTestMember().getName())
//                .role(testUtils.getTestMember().getRole())
//                .email(testUtils.getTestMember().getEmail())
//                .status(testUtils.getTestMember().isStatus())
//                .build();
//
//        MemberAddRequestServiceDto testServiceDto2 = MemberAddRequestServiceDto
//                .builder()
//                .id(testUtils.getTestMember2().getId())
//                .name(testUtils.getTestMember2().getName())
//                .role(testUtils.getTestMember2().getRole())
//                .email(testUtils.getTestMember2().getEmail())
//                .status(testUtils.getTestMember2().isStatus())
//                .build();
//
//        Member testEntity = testServiceDto.toEntity();
//
//        doReturn(true).when(memberRepository).existsByEmail(testEntity.getEmail());
//
//        //when then
//        assertThrows(CustomException.class, () -> {
//            memberService.add(Arrays.asList(testServiceDto,testServiceDto2));
//        });
//    }
}