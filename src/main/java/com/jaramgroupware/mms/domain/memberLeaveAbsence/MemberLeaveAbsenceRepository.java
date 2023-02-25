package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import com.jaramgroupware.mms.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberLeaveAbsenceRepository extends JpaRepository<MemberLeaveAbsence,Integer> {
    Optional<MemberLeaveAbsence> findMemberLeaveAbsenceById(Integer id);
    Optional<MemberLeaveAbsence> findMemberLeaveAbsenceByMember(Member member);
    Optional<List<MemberLeaveAbsence>> findAllBy();

    @Query("SELECT m FROM MEMBER_LEAVE_ABSENCE m WHERE m.id IN :ids")
    List<MemberLeaveAbsence> findAllByIdIn(@Param("ids") Set<Integer> ids);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MEMBER_LEAVE_ABSENCE m WHERE m.id IN :ids")
    int deleteAllByIdInQuery(@Param("ids") Set<Integer> ids);
}
