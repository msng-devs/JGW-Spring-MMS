package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.major.MajorRepository;
import com.jaramgroupware.mms.dto.major.serviceDto.MajorResponseServiceDto;
import com.jaramgroupware.mms.utils.exception.CustomException;
import com.jaramgroupware.mms.utils.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional(readOnly = true)
    public MajorResponseServiceDto findById(Integer id){
        Major targetMajor = majorRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MAJOR_ID));
        return new MajorResponseServiceDto(targetMajor);
    }

    @Transactional(readOnly = true)
    public List<MajorResponseServiceDto> findAll(){
        return majorRepository.findAllBy()
                .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_MAJOR))
                .stream()
                .map(MajorResponseServiceDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MajorResponseServiceDto> findAll(Specification<Major> specification, Pageable pageable){

        return majorRepository.findAll(specification,pageable)
                .stream().map(MajorResponseServiceDto::new)
                .collect(Collectors.toList());
    }

}
