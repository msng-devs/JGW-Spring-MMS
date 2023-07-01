package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.dto.rank.serviceDto.RankResponseServiceDto;
import com.jaramgroupware.mms.utils.exception.CustomException;
import com.jaramgroupware.mms.utils.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 회원 등급에 관한 비즈니스 로직이 들어있는 클래스
 * @since 2023-03-07
 * @author 황준서(37기) hzser123@gmail.com
 * @author 이현희(38기) heeit13145@gmail.com
 */
@RequiredArgsConstructor
@Service
public class RankService {

    private final RankRepository rankRepository;

    /**
     * 단일 회원 등급을 조회하는 함수
     * @param id 조회할 Rank(Object)의 ID
     * @return 조회된 Rank(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 INVALID_RANK_ID 반환
     */
    @Transactional(readOnly = true)
    public RankResponseServiceDto findById(Integer id){
        Rank targetRank = rankRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_RANK_ID));
        return new RankResponseServiceDto(targetRank);
    }

    /**
     * 모든 회원 등급을 조회하는 함수
     * @return 모든 Rank(Object)의 정보를 담은 dto, 해당 데이터가 없을 시 EMPTY_RANK 예외 처리
     */
    @Transactional(readOnly = true)
    public List<RankResponseServiceDto> findAll(){
        return rankRepository.findAllBy()
                .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_RANK))
                .stream()
                .map(RankResponseServiceDto::new)
                .collect(Collectors.toList());
    }

}
