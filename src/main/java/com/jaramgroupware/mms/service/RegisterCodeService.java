package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.registerCode.RegisterCodeRepository;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import static com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode.*;
@Slf4j
@RequiredArgsConstructor
public class RegisterCodeService {
    private final RegisterCodeRepository registerCodeRepository;

    /**
     * 코드를 검증하고 유저 정보를 반환하는 함수
     * @param registerCode 검증할 코드
     * @return 검증된 유저 정보
     * @throws ServiceException NOT_FOUND_CODE 코드가 없을 경우 발생, CODE_EXPIRED, 코드의 만료 시간이 지나면 발생
     */
    @Transactional(rollbackFor = { ServiceException.class })
    public void readRegisterCode(String registerCode){

        var targetRegisterCode = registerCodeRepository.findByCode(registerCode)
                .orElseThrow(() -> new ServiceException(NOT_FOUND_CODE,"올바르지 않은 코드입니다."));

        if(targetRegisterCode.isExpired()) {
            //active 방식으로 만료된 코드에 접근하면 삭제 처리
            registerCodeRepository.delete(targetRegisterCode);
            throw new ServiceException(CODE_EXPIRED,"해당 코드는 만료되었습니다.");
        }
    }
}
