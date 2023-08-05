package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void findById() {
        //given
        doReturn(Optional.of(new Role(1L,"ROLE_USER"))).when(roleRepository).findById(1L);
        //when
        var result = roleService.findById(1L);
        //then
        assertEquals(result.getId(),1L);
        assertEquals(result.getName(),"ROLE_USER");
        verify(roleRepository).findById(1L);

    }

    @Test
    void findAll() {
        //given
        var roles = List.of(
                new Role(1L,"ROLE_USER"),
                new Role(2L,"ROLE_ADMIN")
        );
        doReturn(roles).when(roleRepository).findAllByOrderByIdDesc();

        var excepted = List.of(
                new RoleResponseDto(1L,"ROLE_USER"),
                new RoleResponseDto(2L,"ROLE_ADMIN")
        );
        //when
        var result = roleService.findAll();

        //then
        assertEquals(excepted,result);
        verify(roleRepository).findAllByOrderByIdDesc();
    }
}