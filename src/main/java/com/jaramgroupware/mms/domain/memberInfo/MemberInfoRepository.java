package com.jaramgroupware.mms.domain.memberInfo;

import com.jaramgroupware.mms.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo,String>, JpaSpecificationExecutor<MemberInfo>, MemberInfoCustomRepository {
    Optional<MemberInfo> findMemberInfoById(String id);
    Optional<List<MemberInfo>> findAllBy();
    boolean existsByStudentID(String studentID);
}
