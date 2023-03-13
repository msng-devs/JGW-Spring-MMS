package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.dto.role.controllerDto.RoleResponseControllerDto;
import com.jaramgroupware.mms.dto.role.serviceDto.RoleResponseServiceDto;
import com.jaramgroupware.mms.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Role Api Controller 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class RoleApiController {

    private final RoleService roleService;

    /**
     * 단일 권한 레벨을 조회하는 함수
     * @param roleId 조회할 Role(Object)의 ID
     * @return 조회된 Role(Object)의 정보를 담은 dto 반환
     */
    @GetMapping("{roleId}")
    public ResponseEntity<RoleResponseControllerDto> getRoleById(
            @PathVariable Integer roleId){

        RoleResponseControllerDto result = roleService.findById(roleId).toControllerDto();

        return ResponseEntity.ok(result);
    }

    /**
     * 모든 권한 레벨을 조회하는 함수
     * @return 모든 Role(Object)의 정보를 담은 dto 반환
     */
    @GetMapping
    public ResponseEntity<List<RoleResponseControllerDto>> getRoleAll(){

        List<RoleResponseControllerDto> results = roleService.findAll()
                .stream().map(RoleResponseServiceDto::toControllerDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }


}
