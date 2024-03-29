package com.jaramgroupware.mms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.jaramgroupware.mms.domain")
public class JpaConfig {
}
