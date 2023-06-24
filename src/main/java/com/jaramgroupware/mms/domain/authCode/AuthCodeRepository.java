package com.jaramgroupware.mms.domain.authCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthCodeRepository extends JpaRepository<AuthCode, String> {
    Optional<AuthCode> findById(String id);
}
