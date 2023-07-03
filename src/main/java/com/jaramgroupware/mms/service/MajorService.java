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
     * @return 조회된 major 정보
     * @throws ServiceException 해당 전공 정보가 없을 시 NOT_FOUND 예외 발생
     */
    @Transactional(readOnly = true)
    public MajorResponseDto findById(Long id){
        var targetMajor = majorRepository.findById(id).orElseThrow(() -> new ServiceException(NOT_FOUND,"해당 전공을 찾을 수 없습니다."));
        return new MajorResponseDto(targetMajor);
    }

    /**
     * 모든 전공을 조회하는 함수
     * @return 조회된 major 정보
     */
    @Transactional(readOnly = true)
    public List<MajorResponseDto> findAll(){
        return majorRepository.findAllBy().stream().map(MajorResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 모든 전공을 조회하는 함수
     * @param specification (쿼리 정보, 이름 등)
     * @param pageable 페이징 정보
     * @return 조회된 major 정보
     */
    @Transactional(readOnly = true)
    public List<MajorResponseDto> findAll(Specification<Major> specification, Pageable pageable){

        return majorRepository.findAll(specification,pageable)
                .stream()
                .map(MajorResponseDto::new)
                .collect(Collectors.toList());
    }

}
