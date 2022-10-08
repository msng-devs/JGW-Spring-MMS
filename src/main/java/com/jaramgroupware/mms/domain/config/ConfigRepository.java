package com.jaramgroupware.mms.domain.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config,Integer> {
    Config findConfigByName(String name);
}
