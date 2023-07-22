package com.jaramgroupware.mms.domain.memberView.queryDsl;


import com.jaramgroupware.mms.domain.memberView.MemberView;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface MemberViewQueryDslRepository {
    List<MemberView> findAllWithQueryParams(Pageable pageable, MultiValueMap<String, String> params);
}
