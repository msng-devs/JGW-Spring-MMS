package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.domain.major.MajorSpecification;
import com.jaramgroupware.mms.dto.major.serviceDto.MajorResponseServiceDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

@ComponentScan
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MajorServiceTest {

    @InjectMocks
    private MajorService majorService;

    @Mock
    private MajorRepository majorRepository;

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
        Major testEntity = Major.builder()
                .id(testID)
                .name("test")
                .build();

        doReturn(Optional.of(testEntity)).when(majorRepository).findById(testID);

        //when
        MajorResponseServiceDto result = majorService.findById(testID);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.toString(), Objects.requireNonNull(result).toString());
        verify(majorRepository).findById(testID);
    }

    @Test
    void findAll() {
        //given
        List<Major> testList = new ArrayList<Major>();

        Major testEntity1 = Major.builder()
                .id(1)
                .name("test")
                .build();
        testList.add(testEntity1);

        Major testEntity2 = Major.builder()
                .id(2)
                .name("test")
                .build();
        testList.add(testEntity2);

        doReturn(Optional.of(testList)).when(majorRepository).findAllBy();

        //when
        List<MajorResponseServiceDto> results = majorService.findAll();

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(MajorResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(majorRepository).findAllBy();
    }

    @Test
    void findAllWithSpecAndPage() {
        //given
        List<Major> testList = new ArrayList<Major>();

        Major testEntity1 = Major.builder()
                .id(1)
                .name("test")
                .build();
        testList.add(testEntity1);

        Major testEntity2 = Major.builder()
                .id(2)
                .name("test")
                .build();
        testList.add(testEntity2);

        Specification<Major> testSpec = Mockito.mock(MajorSpecification.class);
        Pageable testPage = Mockito.mock(Pageable.class);
        Page<Major> res = new PageImpl<Major>(testList);

        doReturn(res).when(majorRepository).findAll(testSpec,testPage);

        //when
        List<MajorResponseServiceDto> results = majorService.findAll(testSpec,testPage);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(MajorResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(majorRepository).findAll(testSpec,testPage);
    }
}