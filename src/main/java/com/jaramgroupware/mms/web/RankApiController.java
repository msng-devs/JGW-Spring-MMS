package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.dto.rank.RankResponseDto;

import com.jaramgroupware.mms.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rank Api Controller 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rank")
public class RankApiController {

    private final RankService rankService;

    /**
     * 단일 회원 등급을 조회하는 함수
     * @param rankId 조회할 Rank(Object)의 ID
     * @return 성공적으로 조회 완료 시 해당 Rank(Object)의 정보를 담은 dto 반환
     */
    @GetMapping("{rankId}")
    public ResponseEntity<RankResponseDto> getRankById(
            @PathVariable Long rankId){

        var result = rankService.findById(rankId);

        return ResponseEntity.ok(result);
    }

    /**
     * 모든 회원 등급을 조회하는 함수
     * @return 모든 Rank(Object)의 정보를 담은 dto 반환(List type)
     */
    @GetMapping
    public ResponseEntity<List<RankResponseDto>> getRankAll(){

        List<RankResponseDto> results = rankService.findAll();

        return ResponseEntity.ok(results);
    }


}
