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


@RequiredArgsConstructor
@Service
public class RankService {

    private final RankRepository rankRepository;

    @Transactional(readOnly = true)
    public RankResponseDto findById(Long id){
        var targetRank = rankRepository.findById(id).orElseThrow(() -> new ServiceException(NOT_FOUND,"해당 Rank를 찾을 수 없습니다."));
        return new RankResponseDto(targetRank);
    }


    @Transactional(readOnly = true)
    public List<RankResponseDto> findAll(){
        return rankRepository.findAllBy()
                .stream()
                .map(RankResponseDto::new)
                .collect(Collectors.toList());
    }

}
