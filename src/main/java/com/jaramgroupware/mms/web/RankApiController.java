package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.dto.rank.controllerDto.RankResponseControllerDto;
import com.jaramgroupware.mms.dto.rank.serviceDto.RankResponseServiceDto;
import com.jaramgroupware.mms.service.RankService;
import com.jaramgroupware.mms.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<RankResponseControllerDto> getRankById(
            @PathVariable Integer rankId){

        RankResponseControllerDto result = rankService.findById(rankId).toControllerDto();

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<RankResponseControllerDto>> getRankAll(){

        List<RankResponseControllerDto> results = rankService.findAll()
                .stream().map(RankResponseServiceDto::toControllerDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }


}
