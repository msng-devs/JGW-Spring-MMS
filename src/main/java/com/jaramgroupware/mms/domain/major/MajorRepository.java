package com.jaramgroupware.mms.domain.major;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major,Integer>, JpaSpecificationExecutor<Major> {
    Optional<Major> findMajorById (Integer id);
    Optional<List<Major>> findAllBy();
}
