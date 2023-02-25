package com.jaramgroupware.mms.domain.memberInfo;

import com.jaramgroupware.mms.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo,Integer>, JpaSpecificationExecutor<MemberInfo> {
    Optional<MemberInfo> findMemberInfoById(Integer id);
    Optional<MemberInfo> findMemberInfoByMember(Member member);
    Optional<List<MemberInfo>> findAllBy();
    boolean existsByStudentID(String studentID);

    @Query("SELECT m FROM MEMBER_INFO m JOIN FETCH m.member JOIN FETCH m.major JOIN FETCH m.rank WHERE m.id IN :ids")
    List<MemberInfo> findAllByIdIn(@Param("ids") Set<Integer> ids);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MEMBER_INFO m WHERE m.id IN :ids")
    int deleteAllByIdInQuery(@Param("ids") Set<Integer> ids);
}
