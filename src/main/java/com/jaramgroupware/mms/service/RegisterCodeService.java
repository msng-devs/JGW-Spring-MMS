package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfoRepository;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.domain.registerCode.RegisterCodeRepository;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.dto.registerCode.RegisterCodeResponseDto;
import com.jaramgroupware.mms.dto.registerCode.RegisterResponseDto;
import com.jaramgroupware.mms.dto.registerCode.serviceDto.RegisterCodeAddRequestServiceDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode.*;
@Slf4j
@RequiredArgsConstructor
public class RegisterCodeService {
    private final RegisterCodeRepository registerCodeRepository;
    private final RoleRepository roleRepository;
    private final PreMemberInfoRepository preMemberInfoRepository;

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

        if(targetRegisterCode.isExpired()) {
            //active 방식으로 만료된 코드에 접근하면 삭제 처리
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

        registerCodeRepository.findByPreMemberInfo(targetPreMemberInfo).ifPresent(registerCodeRepository::delete);

        var code = UUID.randomUUID().toString();
        var registerCode = dto.toRegisterCodeEntity(code,targetPreMemberInfo);
        var newRegisterCode = registerCodeRepository.save(registerCode);
        return new RegisterCodeResponseDto(newRegisterCode);
    }
}
