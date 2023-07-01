package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.dto.role.serviceDto.RoleResponseServiceDto;
import com.jaramgroupware.mms.utils.exception.CustomException;
import com.jaramgroupware.mms.utils.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 권한 레벨에 관한 비즈니스 로직이 들어있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * 단일 권한 레벨을 조회하는 함수
     * @param id 조회할 Role(Object)의 ID
     * @return 조회된 Role(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_ROLE_ID 예외 처리
     */
    @Transactional(readOnly = true)
    public RoleResponseServiceDto findById(Integer id){
        Role targetRole = roleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ROLE_ID));
        return new RoleResponseServiceDto(targetRole);
    }

    /**
     * 모든 권한 레벨을 조회하는 함수
     * @return 모든 Role(Object)의 정보를 담은 dto(List type), 해당 데이터가 없을 시 EMPTY_ROLE 예외 처리
     */
    @Transactional(readOnly = true)
    public List<RoleResponseServiceDto> findAll(){
        return roleRepository.findAllBy()
                .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_ROLE))
                .stream()
                .map(RoleResponseServiceDto::new)
                .collect(Collectors.toList());
    }

}
