package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.dto.rank.serviceDto.RankResponseServiceDto;
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
class RankServiceTest {

    @InjectMocks
    private RankService rankService;

    @Mock
    private RankRepository rankRepository;

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
        Rank testEntity = Rank.builder()
                .id(testID)
                .name("test")
                .build();

        doReturn(Optional.of(testEntity)).when(rankRepository).findById(testID);

        //when
        RankResponseServiceDto result = rankService.findById(testID);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.toString(), Objects.requireNonNull(result).toString());
        verify(rankRepository).findById(testID);
    }

    @Test
    void findAll() {
        //given
        List<Rank> testList = new ArrayList<Rank>();

        Rank testEntity1 = Rank.builder()
                .id(1)
                .name("test")
                .build();
        testList.add(testEntity1);

        Rank testEntity2 = Rank.builder()
                .id(2)
                .name("test")
                .build();
        testList.add(testEntity2);

        doReturn(Optional.of(testList)).when(rankRepository).findAllBy();

        //when
        List<RankResponseServiceDto> results = rankService.findAll();

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(testList.stream().map(RankResponseServiceDto::new).collect(Collectors.toList()).toString(), Objects.requireNonNull(results).toString());
        verify(rankRepository).findAllBy();
    }
}