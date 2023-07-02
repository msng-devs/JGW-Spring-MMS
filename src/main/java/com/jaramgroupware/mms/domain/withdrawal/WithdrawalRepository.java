package com.jaramgroupware.mms.domain.withdrawal;

import com.jaramgroupware.mms.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Integer>{
    Optional<Withdrawal> findByMember(Member member);
}
