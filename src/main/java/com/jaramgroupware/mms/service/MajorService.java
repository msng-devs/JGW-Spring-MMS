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

/**
 * 전공에 관한 비즈니스 로직이 들어있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@Service
public class MajorService {

    private final MajorRepository majorRepository;

    /**
     * 단일 전공을 조회하는 함수
     * @param id 조회할 Major(Object)의 ID
     * @return 조회된 Major(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_MAJOR_ID 예외 처리
     */
    @Transactional(readOnly = true)
    public MajorResponseServiceDto findById(Integer id){
        Major targetMajor = majorRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MAJOR_ID));
        return new MajorResponseServiceDto(targetMajor);
    }

    /**
     * 모든 전공을 조회하는 함수
     * @return 모든 Major(Object)의 정보를 담은 dto(List type), 해당 데이터가 없을 시 EMPTY_MAJOR 예외 처리
     */
    @Transactional(readOnly = true)
    public List<MajorResponseServiceDto> findAll(){
        return majorRepository.findAllBy()
                .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_MAJOR))
                .stream()
                .map(MajorResponseServiceDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 모든 전공을 조회하는 함수 (Query Options, Page Options)
     * @param specification query option
     * @param pageable sort option
     * @return 모든 Major(Object)의 정보를 담은 dto(List type)
     */
    @Transactional(readOnly = true)
    public List<MajorResponseServiceDto> findAll(Specification<Major> specification, Pageable pageable){

        return majorRepository.findAll(specification,pageable)
                .stream().map(MajorResponseServiceDto::new)
                .collect(Collectors.toList());
    }

}
