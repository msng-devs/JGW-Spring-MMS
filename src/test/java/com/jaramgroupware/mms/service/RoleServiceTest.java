package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.domain.role.RoleRepository;
import com.jaramgroupware.mms.dto.role.serviceDto.RoleResponseServiceDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findById() {
        //given
        Integer testID = 1;
        Role testEntity = Role.builder()
                .id(testID)
                .name("test")
                .build();

        doReturn(Optional.of(testEntity)).when(roleRepository).findById(testID);

        //when
        RoleResponseServiceDto result = roleService.findById(testID);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.toString(), Objects.requireNonNull(result).toString());
        verify(roleRepository).findById(testID);
    }

    @Test
    void findAll() {
        //given
        List<Role> testList = new ArrayList<Role>();

        Role testEntity1 = Role.builder()
                .id(1)
                .name("test")
                .build();
        testList.add(testEntity1);

        Role testEntity2 = Role.builder()
                .id(2)
                .name("test")
                .build();
        testList.add(testEntity2);

        doReturn(Optional.of(testList)).when(roleRepository).findAllBy();

        //when
        List<RoleResponseServiceDto> results = roleService.findAll();

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(RoleResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(roleRepository).findAllBy();
    }
}