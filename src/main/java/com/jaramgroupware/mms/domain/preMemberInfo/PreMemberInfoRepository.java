package com.jaramgroupware.mms.domain.preMemberInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PreMemberInfoRepository extends JpaRepository<PreMemberInfo, Long>, JpaSpecificationExecutor<PreMemberInfo> {
    boolean existsByStudentId(String studentId);
}
