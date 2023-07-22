package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.MajorRepository;

import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//    @Transactional(readOnly = true)
//    public List<MajorResponseDto> findAll(Specification<Major> specification, Pageable pageable){
//
//        return majorRepository.findAll(specification,pageable)
//                .stream()
//                .map(MajorResponseDto::new)
//                .collect(Collectors.toList());
//    }

}
