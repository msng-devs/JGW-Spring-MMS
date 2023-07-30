package com.jaramgroupware.mms.domain.memberView;


import com.jaramgroupware.mms.domain.memberView.queryDsl.MemberViewQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberViewRepository  extends JpaRepository<MemberView, String>, JpaSpecificationExecutor<MemberView>, MemberViewQueryDslRepository {
}
