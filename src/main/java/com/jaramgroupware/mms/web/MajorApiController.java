package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.domain.major.MajorSpecificationBuilder;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.service.MajorService;
import com.jaramgroupware.mms.utils.validation.page.PageableSortKeyCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Major Api Controller 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/major")
public class MajorApiController {

    private final MajorService majorService;
    private final MajorSpecificationBuilder majorSpecificationBuilder;

    /**
     * 단일 전공을 조회하는 함수
     * @param majorId 조회할 Major(Object)의 ID
     * @return 성공적으로 조회 완료 시 해당 Major(Object)의 정보를 담은 dto 반환
     */
    @GetMapping("{majorId}")
    public ResponseEntity<MajorResponseDto> getMajorById(
            @PathVariable Integer majorId){

        var result = majorService.findById(majorId);

        return ResponseEntity.ok(result);
    }

    /**
     * 다수 전공을 조회하는 함수
     * @param pageable sort option
     * @param queryParam query option
     * @return 성공적으로 조회 완료 시 해당 Major(Object)의 정보를 담은 dto들을 반환(List type)
     */
    @GetMapping
    public ResponseEntity<List<MajorResponseDto>> getMajorAll(
            @PageableDefault(page = 0,size = 100,sort = "id",direction = Sort.Direction.DESC)
            @PageableSortKeyCheck(sortKeys = {"id","name"}) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> queryParam) {

        var spec = majorSpecificationBuilder.toSpec(queryParam);
        var results = majorService.findAll(spec,pageable);

        return ResponseEntity.ok(results);
    }
}
