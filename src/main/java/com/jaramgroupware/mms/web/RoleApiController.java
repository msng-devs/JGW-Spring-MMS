package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.dto.role.controllerDto.RoleResponseControllerDto;
import com.jaramgroupware.mms.dto.role.serviceDto.RoleResponseServiceDto;
import com.jaramgroupware.mms.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class RoleApiController {

    private final RoleService roleService;

    @GetMapping("{roleId}")
    public ResponseEntity<RoleResponseControllerDto> getRoleById(
            @PathVariable Integer roleId){

        RoleResponseControllerDto result = roleService.findById(roleId).toControllerDto();

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseControllerDto>> getRoleAll(){

        List<RoleResponseControllerDto> results = roleService.findAll()
                .stream().map(RoleResponseServiceDto::toControllerDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }


}
