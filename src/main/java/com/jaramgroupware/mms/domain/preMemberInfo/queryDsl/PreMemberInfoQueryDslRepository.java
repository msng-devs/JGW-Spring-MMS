package com.jaramgroupware.mms.domain.preMemberInfo.queryDsl;

import com.jaramgroupware.mms.domain.memberView.MemberView;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface PreMemberInfoQueryDslRepository {
    List<PreMemberInfo> findAllWithQueryParams(Pageable pageable, MultiValueMap<String, String> params);
    Integer countAllWithQueryParams(MultiValueMap<String, String> params);
}
