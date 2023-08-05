package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfoRepository;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.domain.registerCode.RegisterCodeRepository;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.preMemberInfo.PreMemberInfoResponseDto;
import com.jaramgroupware.mms.dto.preMemberInfo.serviceDto.PreMemberInfoAddRequestServiceDto;
import com.jaramgroupware.mms.dto.preMemberInfo.serviceDto.PreMemberInfoUpdateRequestServiceDto;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class PreMemberInfoServiceTest {
    @InjectMocks
    private PreMemberInfoService preMemberInfoService;
    @Mock
    private PreMemberInfoRepository preMemberInfoRepository;
    @Mock
    private MajorRepository majorRepository;
    @Mock
    private RankRepository rankRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private RegisterCodeRepository registerCodeRepository;
    @Test
    void findById() {
        //given
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

        doReturn(Optional.of(preMemberInfo)).when(preMemberInfoRepository).findById(1L);

        var except = PreMemberInfoResponseDto
                .builder()
                .id(1L)
                .studentId("201510302")
                .major(new MajorResponseDto(1L,"테스트 학과"))
                .rank(new RankResponseDto(1L,"테스트 랭크"))
                .role(new RoleResponseDto(1L,"ROLE_GUEST"))
                .year(34)
                .name("김개발")
                .build();
        //when
        var result = preMemberInfoService.findById(1L);

        //then
        assertEquals(except,result);
        verify(preMemberInfoRepository).findById(1L);
    }

    @Test
    void findAll() {
        //given
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

        var preMemberInfo2 = PreMemberInfo
                .builder()
                .id(2L)
                .registerCode(null)
                .studentId("201510302")
                .expectedDateReturnSchool(null)
                .major(new Major(1L,"테스트 학과"))
                .rank(new Rank(1L,"테스트 랭크"))
                .year(34)
                .name("김개발")
                .role(new Role(1L,"ROLE_GUEST"))
                .build();

        var pageable = Mockito.mock(Pageable.class);
        var params = new LinkedMultiValueMap<String,String>();

        var preMemberInfoList = Arrays.asList(preMemberInfo,preMemberInfo2);
        doReturn(preMemberInfoList).when(preMemberInfoRepository).findAllWithQueryParams(pageable,params);

        var except = PreMemberInfoResponseDto
                .builder()
                .id(1L)
                .studentId("201510302")
                .major(new MajorResponseDto(1L,"테스트 학과"))
                .rank(new RankResponseDto(1L,"테스트 랭크"))
                .role(new RoleResponseDto(1L,"ROLE_GUEST"))
                .year(34)
                .name("김개발")
                .build();

        var except2 = PreMemberInfoResponseDto
                .builder()
                .id(2L)
                .studentId("201510302")
                .major(new MajorResponseDto(1L,"테스트 학과"))
                .rank(new RankResponseDto(1L,"테스트 랭크"))
                .role(new RoleResponseDto(1L,"ROLE_GUEST"))
                .year(34)
                .name("김개발")
                .build();

        var exceptList = Arrays.asList(except,except2);
        //when
        var result = preMemberInfoService.findAll(params,pageable);

        //then
        assertEquals(exceptList,result);
        verify(preMemberInfoRepository).findAllWithQueryParams(pageable,params);
    }

    @Test
    void deletePreMemberInfo() {
        //given
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

        doReturn(Optional.of(preMemberInfo)).when(preMemberInfoRepository).findById(1L);
        //when
        preMemberInfoService.deletePreMemberInfo(1L);
        //then
        verify(preMemberInfoRepository).delete(preMemberInfo);
        verify(preMemberInfoRepository).findById(1L);

    }

    @Test
    void createPreMemberInfo() {
        //given
        var requestDto = PreMemberInfoAddRequestServiceDto
                .builder()
                .studentId("201510302")
                .majorId(1L)
                .rankId(1L)
                .roleId(1L)
                .year(34)
                .name("김개발")
                .expectedDateReturnSchool(null)
                .expireDay(1L)
                .build();

        doReturn(Optional.of(new Role(1L,"ROLE_USER"))).when(roleRepository).findById(1L);
        doReturn(Optional.of(new Rank(1L,"테스트 랭크"))).when(rankRepository).findById(1L);
        doReturn(Optional.of(new Major(1L,"테스트 학과"))).when(majorRepository).findById(1L);

        doReturn(false).when(preMemberInfoRepository).existsByStudentId(requestDto.getStudentId());

        var target = PreMemberInfo
                .builder()
                .studentId("201510302")
                .major(new Major(1L,"테스트 학과"))
                .rank(new Rank(1L,"테스트 랭크"))
                .year(34)
                .name("김개발")
                .role(new Role(1L,"ROLE_USER"))
                .expectedDateReturnSchool(null)
                .build();

        var exceptTarget = PreMemberInfo
                .builder()
                .id(1L)
                .studentId("201510302")
                .major(new Major(1L,"테스트 학과"))
                .rank(new Rank(1L,"테스트 랭크"))
                .year(34)
                .name("김개발")
                .role(new Role(1L,"ROLE_USER"))
                .expectedDateReturnSchool(null)
                .build();

        doReturn(exceptTarget).when(preMemberInfoRepository).save(target);

        var except = PreMemberInfoResponseDto
                .builder()
                .id(1L)
                .studentId("201510302")
                .major(new MajorResponseDto(1L,"테스트 학과"))
                .rank(new RankResponseDto(1L,"테스트 랭크"))
                .role(new RoleResponseDto(1L,"ROLE_USER"))
                .year(34)
                .name("김개발")
                .build();
        //when
        var result = preMemberInfoService.createPreMemberInfo(requestDto);

        //then
        verify(roleRepository).findById(1L);
        verify(rankRepository).findById(1L);
        verify(majorRepository).findById(1L);
        verify(preMemberInfoRepository).existsByStudentId(requestDto.getStudentId());
        verify(preMemberInfoRepository).save(target);
        verify(memberService).isExistsStudentId(requestDto.getStudentId());
    }

    @Test
    void updatePreMemberInfo() {
        //given
        var requestDto = PreMemberInfoUpdateRequestServiceDto
                .builder()
                .studentId("201510302")
                .majorId(1L)
                .rankId(1L)
                .roleId(1L)
                .year(34)
                .name("김개발2")
                .expectedDateReturnSchool(null)
                .id(1L)
                .build();

        var target = PreMemberInfo
                .builder()
                .id(1L)
                .studentId("201510302")
                .major(new Major(1L,"테스트 학과"))
                .rank(new Rank(1L,"테스트 랭크"))
                .year(34)
                .name("김개발")
                .role(new Role(1L,"ROLE_USER"))
                .expectedDateReturnSchool(null)
                .build();

        doReturn(Optional.of(target)).when(preMemberInfoRepository).findById(1L);

        doReturn(Optional.of(new Role(1L,"ROLE_USER"))).when(roleRepository).findById(1L);
        doReturn(Optional.of(new Rank(1L,"테스트 랭크"))).when(rankRepository).findById(1L);
        doReturn(Optional.of(new Major(1L,"테스트 학과"))).when(majorRepository).findById(1L);

        var updatedTarget = PreMemberInfo
                .builder()
                .id(1L)
                .studentId("201510302")
                .major(new Major(1L,"테스트 학과"))
                .rank(new Rank(1L,"테스트 랭크"))
                .year(34)
                .name("김개발2")
                .role(new Role(1L,"ROLE_USER"))
                .expectedDateReturnSchool(null)
                .build();

        doReturn(updatedTarget).when(preMemberInfoRepository).save(updatedTarget);

        var except = PreMemberInfoResponseDto
                .builder()
                .id(1L)
                .studentId("201510302")
                .major(new MajorResponseDto(1L,"테스트 학과"))
                .rank(new RankResponseDto(1L,"테스트 랭크"))
                .role(new RoleResponseDto(1L,"ROLE_USER"))
                .year(34)
                .name("김개발2")
                .build();

        //when
        var result = preMemberInfoService.updatePreMemberInfo(requestDto);

        //then
        assertEquals(except,result);
        verify(preMemberInfoRepository).findById(1L);
        verify(roleRepository).findById(1L);
        verify(rankRepository).findById(1L);
        verify(majorRepository).findById(1L);
        verify(preMemberInfoRepository).save(updatedTarget);

    }
}