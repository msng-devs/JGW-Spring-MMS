package com.jaramgroupware.mms.service;

import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.rank.RankRepository;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode;
import com.jaramgroupware.mms.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jaramgroupware.mms.utils.exception.service.ServiceErrorCode.NOT_FOUND;

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
     * @return 조회된 Rank(Object)의 정보를 담은 dto
     * @throws ServiceException 해당 데이터가 없을 시 NOT_FOUND 에러 발생
     */
    @Transactional(readOnly = true)
    public RankResponseDto findById(Integer id){
        Rank targetRank = rankRepository.findById(id).orElseThrow(() -> new ServiceException(NOT_FOUND,"해당 Rank를 찾을 수 없습니다."));
        return new RankResponseDto(targetRank);
    }

    /**
     * 모든 회원 등급을 조회하는 함수
     * @return 모든 Rank(Object)의 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    public List<RankResponseDto> findAll(){
        return rankRepository.findAllBy()
                .stream()
                .map(RankResponseDto::new)
                .collect(Collectors.toList());
    }

}
