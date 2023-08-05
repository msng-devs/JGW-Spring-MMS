package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;

import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import static com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode.*;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public RoleResponseDto findById(Long id){
        var targetRole = roleRepository.findById(id)
                .orElseThrow(() -> new ServiceException(NOT_FOUND,"해당 Role을 찾을 수 없습니다."));
        return new RoleResponseDto(targetRole);
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDto> findAll(){
        return roleRepository.findAllByOrderByIdDesc()
                .stream()
                .map(RoleResponseDto::new)
                .collect(Collectors.toList());
    }

}
