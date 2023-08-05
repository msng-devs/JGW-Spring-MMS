package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class RankServiceTest {

    @Mock
    private RankRepository rankRepository;

    @InjectMocks
    private RankService rankService;

    @Test
    void findById() {
        //given
        var target = Rank.builder()
                .id(1L)
                .name("정회원")
                .build();

        doReturn(Optional.of(target)).when(rankRepository).findById(1L);

        var except = RankResponseDto.builder()
                .id(1L)
                .name("정회원")
                .build();
        //when
        var result = rankService.findById(1L);

        //then
        assertEquals(except, result);

    }

    @Test
    void findAll() {
        //given
        var target = Rank.builder()
                .id(1L)
                .name("정회원")
                .build();
        var target2 = Rank.builder()
                .id(2L)
                .name("OB")
                .build();

        var rankList = List.of(target, target2);

        doReturn(rankList).when(rankRepository).findAllBy();

        var except = List.of(
                RankResponseDto.builder()
                        .id(1L)
                        .name("정회원")
                        .build(),
                RankResponseDto.builder()
                        .id(2L)
                        .name("OB")
                        .build()
        );
        //when
        var result = rankService.findAll();

        //then
        assertEquals(except, result);

    }
}