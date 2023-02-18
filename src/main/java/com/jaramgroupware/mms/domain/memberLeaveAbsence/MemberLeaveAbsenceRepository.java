package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberLeaveAbsenceRepository extends JpaRepository<MemberLeaveAbsence,String> {
    Optional<MemberLeaveAbsence> findMemberLeaveAbsenceById(String id);
    Optional<List<MemberLeaveAbsence>> findAllBy();
}
