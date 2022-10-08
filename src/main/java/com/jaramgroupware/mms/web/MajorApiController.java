package com.jaramgroupware.mms.web;

import com.jaramgroupware.mms.domain.major.MajorSpecification;
import com.jaramgroupware.mms.domain.major.MajorSpecificationBuilder;
import com.jaramgroupware.mms.dto.major.controllerDto.MajorResponseControllerDto;
import com.jaramgroupware.mms.dto.major.serviceDto.MajorResponseServiceDto;
import com.jaramgroupware.mms.service.MajorService;
import com.jaramgroupware.mms.utils.validation.PageableValid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/major")
public class MajorApiController {

    private final MajorService majorService;
    private final MajorSpecificationBuilder majorSpecificationBuilder;

    @GetMapping("{majorId}")
    public ResponseEntity<MajorResponseControllerDto> getMajorById(
            @PathVariable Integer majorId){

        MajorResponseControllerDto result = majorService.findById(majorId).toControllerDto();

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<MajorResponseControllerDto>> getMajorAll(
            @PageableDefault(page = 0,size = 1000,sort = "id",direction = Sort.Direction.DESC)
            @PageableValid(sortKeys = {"id","name"}) Pageable pageable,
            @RequestParam(required = false) MultiValueMap<String, String> queryParam) {


        //limit 확인 및 추가
        int limit = queryParam.containsKey("limit") ? Integer.parseInt(Objects.requireNonNull(queryParam.getFirst("limit"))) : -1;

        //Specification 등록
        MajorSpecification spec = majorSpecificationBuilder.toSpec(queryParam);

        List<MajorResponseControllerDto> results;

        //limit true
        if(limit > 0){
            results = majorService.findAll(spec, PageRequest.of(0, limit, pageable.getSort()))
                    .stream()
                    .map(MajorResponseServiceDto::toControllerDto)
                    .collect(Collectors.toList());
        }

        else{
            results = majorService.findAll(spec,pageable)
                    .stream()
                    .map(MajorResponseServiceDto::toControllerDto)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(results);
    }
}
