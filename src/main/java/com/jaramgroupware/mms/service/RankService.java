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

@RequiredArgsConstructor
@Service
public class RankService {

    private final RankRepository rankRepository;

    @Transactional(readOnly = true)
    public RankResponseServiceDto findById(Integer id){
        Rank targetRank = rankRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_RANK_ID));
        return new RankResponseServiceDto(targetRank);
    }

    @Transactional(readOnly = true)
    public List<RankResponseServiceDto> findAll(){
        return rankRepository.findAllBy()
                .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_RANK))
                .stream()
                .map(RankResponseServiceDto::new)
                .collect(Collectors.toList());
    }

}
