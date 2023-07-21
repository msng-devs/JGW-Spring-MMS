package com.jaramgroupware.mms.domain.major;

import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface MajorQueryDslRepository {
    List<Major> findAllWithQueryParams(Pageable pageable, MultiValueMap<String, String> params);
}
