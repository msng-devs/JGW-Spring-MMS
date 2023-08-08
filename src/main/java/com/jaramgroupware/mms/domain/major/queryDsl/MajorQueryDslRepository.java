package com.jaramgroupware.mms.domain.major.queryDsl;

import com.jaramgroupware.mms.domain.major.Major;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface MajorQueryDslRepository{
    List<Major> findAllWithQueryParams(Pageable pageable, MultiValueMap<String, String> params);
    Integer countAllWithQueryParams(MultiValueMap<String, String> params);

}
