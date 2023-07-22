package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.dto.role.RoleResponseDto;

import com.jaramgroupware.mms.service.RoleService;
import com.jaramgroupware.mms.utils.aop.routeOption.auth.AuthOption;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class RoleApiController {

    private final RoleService roleService;

    @AuthOption
    @GetMapping("{roleId}")
    public ResponseEntity<RoleResponseDto> getRoleById(
            @PathVariable Long roleId){

        var result = roleService.findById(roleId);

        return ResponseEntity.ok(result);
    }

    @AuthOption
    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getRoleAll(){
        List<RoleResponseDto> results = roleService.findAll();
        return ResponseEntity.ok(results);
    }


}
