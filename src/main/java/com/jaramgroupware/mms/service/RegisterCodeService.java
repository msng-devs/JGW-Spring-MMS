package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfoRepository;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.domain.registerCode.RegisterCodeRepository;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.dto.registerCode.RegisterCodeResponseDto;
import com.jaramgroupware.mms.dto.registerCode.serviceDto.RegisterCodeAddRequestServiceDto;
import com.jaramgroupware.mms.utils.code.CodeGenerator;
import com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode.*;
@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterCodeService {
    private final RegisterCodeRepository registerCodeRepository;
    private final RoleRepository roleRepository;
    private final PreMemberInfoRepository preMemberInfoRepository;
    private final TimeUtility timeUtility;
    private final CodeGenerator codeGenerator;
    /**
     * 코드를 검증하고 유저 정보를 반환하는 함수
     * @param registerCode 검증할 코드
     * @return 검증된 유저 정보
     * @throws ServiceException NOT_FOUND_CODE 코드가 없을 경우 발생, CODE_EXPIRED, 코드의 만료 시간이 지나면 발생
     */
    @Transactional(rollbackFor = { ServiceException.class })
    public RegisterCodeResponseDto readRegisterCode(String registerCode){

        var targetRegisterCode = registerCodeRepository.findByCode(registerCode)
                .orElseThrow(() -> new ServiceException(NOT_FOUND_CODE,"올바르지 않은 코드입니다."));

        if(targetRegisterCode.isExpired(timeUtility.nowDate())) {
            registerCodeRepository.delete(targetRegisterCode);
            throw new ServiceException(CODE_EXPIRED,"해당 코드는 만료되었습니다.");
        }

        return new RegisterCodeResponseDto(targetRegisterCode);
    }

    @Transactional
    public void deleteRegisterCode(Long preMemberInfoId){
        var targetPreMemberInfo = preMemberInfoRepository.findById(preMemberInfoId)
                .orElseThrow(() -> new ServiceException(NOT_FOUND,"해당 사전 회원 정보가 존재하지 않습니다."));

        var targetRegisterCode = registerCodeRepository.findByPreMemberInfo(targetPreMemberInfo)
                .orElseThrow(() -> new ServiceException(NOT_FOUND,"해당 코드가 존재하지 않습니다."));

        registerCodeRepository.delete(targetRegisterCode);
    }

    @Transactional
    public RegisterCodeResponseDto createRegisterCode(RegisterCodeAddRequestServiceDto dto){
        var targetPreMemberInfo = preMemberInfoRepository.findById(dto.getPreMemberInfoId())
                .orElseThrow(() -> new ServiceException(NOT_FOUND,"해당 사전 회원 정보가 존재하지 않습니다."));

        registerCodeRepository.findByPreMemberInfo(targetPreMemberInfo)
                .ifPresent(registerCode -> {
                    throw new ServiceException(ALREADY_EXISTS,"이미 코드가 존재합니다.");
                });

        var code = codeGenerator.generate();
        var registerCode = RegisterCode.builder()
                .code(code)
                .preMemberInfo(targetPreMemberInfo)
                .createBy(dto.getCreatedBy())
                .expiredAt(timeUtility.nowDate().plusDays(dto.getExpireDay()))
                .build();

        var newRegisterCode = registerCodeRepository.save(registerCode);
        return new RegisterCodeResponseDto(newRegisterCode);
    }

    @Transactional(readOnly = true)
    public Integer findAllExpiredAndDel(LocalDate nowDate){
        var targets = registerCodeRepository.findAllExpired(nowDate);
        if(targets.isEmpty()) return 0;
        registerCodeRepository.deleteAllByCodeIn(targets.stream().map(RegisterCode::getCode).toList());
        return targets.size();
    }
}
