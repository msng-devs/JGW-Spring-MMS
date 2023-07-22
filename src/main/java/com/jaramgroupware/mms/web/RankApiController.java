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


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rank")
public class RankApiController {

    private final RankService rankService;

    @GetMapping("{rankId}")
    public ResponseEntity<RankResponseDto> getRankById(
            @PathVariable Long rankId){

        var result = rankService.findById(rankId);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<RankResponseDto>> getRankAll(){

        List<RankResponseDto> results = rankService.findAll();

        return ResponseEntity.ok(results);
    }


}
