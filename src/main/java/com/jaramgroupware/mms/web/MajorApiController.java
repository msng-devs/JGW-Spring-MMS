package com.jaramgroupware.mms.web;


import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.service.MajorService;
import com.jaramgroupware.mms.utils.validation.page.PageableSortKeyCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/mms/api/v1/major")
public class MajorApiController {

    private final MajorService majorService;

    @GetMapping("{majorId}")
    public ResponseEntity<MajorResponseDto> getMajorById(
            @PathVariable Long majorId){

        var result = majorService.findById(majorId);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<MajorResponseDto>> getMajorAll(
            @PageableDefault(page = 0,size = 100,sort = "id",direction = Sort.Direction.DESC)
            @PageableSortKeyCheck(sortKeys = {"id","name"}) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> queryParam) {

        var results = majorService.findAll(queryParam,pageable);

        return ResponseEntity.ok(results);
    }
}
