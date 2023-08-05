package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfoRepository;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.domain.registerCode.RegisterCodeRepository;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.dto.registerCode.RegisterCodeResponseDto;
import com.jaramgroupware.mms.dto.registerCode.serviceDto.RegisterCodeAddRequestServiceDto;
import com.jaramgroupware.mms.utils.code.CodeGenerator;
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
class RegisterCodeServiceTest {

    @Mock
    private RegisterCodeRepository registerCodeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PreMemberInfoRepository preMemberInfoRepository;

    @Mock
    private TimeUtility timeUtility;

    @Mock
    private CodeGenerator codeGenerator;

    @InjectMocks
    private RegisterCodeService registerCodeService;

    private final String testCode = "2a91b37f-99db-4193-8ef6-738a80434c4c";
    @Test
    void readRegisterCode() {
        //given
        var preMemberInfo = PreMemberInfo
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

        var target = RegisterCode.builder()
                .code(testCode)
                .preMemberInfo(preMemberInfo)
                .createBy("test")
                .expiredAt(LocalDate.of(2021, 12, 31))
                .build();

        doReturn(Optional.of(target)).when(registerCodeRepository).findByCode(testCode);
        doReturn(LocalDate.of(2021, 12, 30)).when(timeUtility).nowDate();

        var except = RegisterCodeResponseDto.builder()
                .code(testCode)
                .preMemberInfoId(1L)
                .expiredDateTime(LocalDate.of(2021, 12, 31))
                .build();
        //when
        var result = registerCodeService.readRegisterCode(testCode);

        //then
        assertEquals(except, result);
        verify(registerCodeRepository).findByCode(testCode);
        verify(timeUtility).nowDate();
    }

    @Test
    void deleteRegisterCode() {
        //given
        var preMemberInfo = PreMemberInfo
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

        doReturn(Optional.of(preMemberInfo)).when(preMemberInfoRepository).findById(1L);

        var target = RegisterCode.builder()
                .code(testCode)
                .preMemberInfo(preMemberInfo)
                .createBy("test")
                .expiredAt(LocalDate.of(2021, 12, 31))
                .build();

        doReturn(Optional.of(target)).when(registerCodeRepository).findByPreMemberInfo(preMemberInfo);
        //when
        registerCodeService.deleteRegisterCode(1L);

        //then
        verify(preMemberInfoRepository).findById(1L);
        verify(registerCodeRepository).findByPreMemberInfo(preMemberInfo);
        verify(registerCodeRepository).delete(target);
    }

    @Test
    void createRegisterCode() {
        //given
        var request = RegisterCodeAddRequestServiceDto.builder()
                .preMemberInfoId(1L)
                .expireDay(1L)
                .createdBy("test")
                .build();

        var preMemberInfo = PreMemberInfo
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

        var target = RegisterCode.builder()
                .code(testCode)
                .preMemberInfo(preMemberInfo)
                .createBy("test")
                .expiredAt(LocalDate.of(2021, 12, 31))
                .build();

        doReturn(Optional.of(preMemberInfo)).when(preMemberInfoRepository).findById(1L);
        doReturn(Optional.of(target)).when(registerCodeRepository).findByPreMemberInfo(preMemberInfo);
        doReturn(testCode).when(codeGenerator).generate();
        doReturn(LocalDate.of(2021, 12, 30)).when(timeUtility).nowDate();

        var registerCode = RegisterCode.builder()
                .code(testCode)
                .createBy("test")
                .preMemberInfo(preMemberInfo)
                .expiredAt(LocalDate.of(2021, 12, 31))
                .build();

        doReturn(registerCode).when(registerCodeRepository).save(registerCode);

        //when
        var result = registerCodeService.createRegisterCode(request);

        //then
        assertEquals(testCode, result.getCode());
        verify(preMemberInfoRepository).findById(1L);
        verify(registerCodeRepository).findByPreMemberInfo(preMemberInfo);
        verify(codeGenerator).generate();
        verify(timeUtility).nowDate();
        verify(registerCodeRepository).save(registerCode);
        verify(registerCodeRepository).delete(target);
    }
}