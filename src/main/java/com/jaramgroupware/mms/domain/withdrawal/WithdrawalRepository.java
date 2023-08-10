package com.jaramgroupware.mms.domain.withdrawal;

import com.jaramgroupware.mms.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Integer>{
    Optional<Withdrawal> findByMember(Member member);
    @Query("SELECT w FROM WITHDRAWAL w WHERE w.withdrawalDate <= :expiredDate")
    List<Withdrawal> findAllExpired(@Param("expiredDate") LocalDate expiredDate);
}
