package com.jaramgroupware.mms.domain.memberView;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberViewRepository  extends JpaRepository<MemberView, String>, JpaSpecificationExecutor<MemberView> {
}
