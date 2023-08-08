package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class MajorServiceTest {

    @InjectMocks
    private MajorService majorService;

    @Mock
    private MajorRepository majorRepository;

    @Test
    void findById() {
        //given
        var target = new Major(1L,"인공지능학과");

        doReturn(Optional.of(target)).when(majorRepository).findById(1L);

        var expected = MajorResponseDto.builder()
                .id(1L)
                .name("인공지능학과")
                .build();
        //when
        var result = majorService.findById(1L);

        //then
        assertEquals(expected,result);
        verify(majorRepository).findById(1L);
    }

    @Test
    void findAll() {
        //given
        var target = Arrays.asList(new Major(1L,"인공지능학과"),new Major(2L,"컴퓨터공학과"));
        var mockPageable = Mockito.mock(Pageable.class);
        var queryParams = new LinkedMultiValueMap<String,String>();

        doReturn(target).when(majorRepository).findAllWithQueryParams(mockPageable,queryParams);

        var exceptResult = Arrays.asList(
                MajorResponseDto.builder()
                        .id(1L)
                        .name("인공지능학과")
                        .build(),
                MajorResponseDto.builder()
                        .id(2L)
                        .name("컴퓨터공학과")
                        .build()
        );
        //when
        var result = majorService.findAll(queryParams,mockPageable);

        //then
        assertEquals(2,result.getContent().size());
        assertArrayEquals(exceptResult.toArray(), result.getContent().toArray());
        verify(majorRepository).findAllWithQueryParams(mockPageable,queryParams);
    }
}