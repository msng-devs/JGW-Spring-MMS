package com.jaramgroupware.mms.domain.error;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends JpaRepository<Error,Integer> {
    Error findErrorByName(String name);
}
