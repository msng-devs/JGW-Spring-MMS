package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.MajorRepository;

import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

import static com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode.*;

@RequiredArgsConstructor
@Service
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional(readOnly = true)
    public MajorResponseDto findById(Long id){
        var targetMajor = majorRepository.findById(id).orElseThrow(() -> new ServiceException(NOT_FOUND,"해당 전공을 찾을 수 없습니다."));
        return new MajorResponseDto(targetMajor);
    }

    @Transactional(readOnly = true)
    public Page<MajorResponseDto> findAll(MultiValueMap<String,String> queryParam, Pageable pageable){

        var items = majorRepository.findAllWithQueryParams(pageable,queryParam)
                .stream()
                .map(MajorResponseDto::new)
                .toList();

        var total = majorRepository.countAllWithQueryParams(queryParam);

        return new PageImpl<>(items,pageable,total);

    }

}
