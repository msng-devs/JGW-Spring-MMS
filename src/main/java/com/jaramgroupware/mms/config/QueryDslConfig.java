//package com.jaramgroupware.mms.config;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//
//@EnableJpaAuditing
//@Configuration
//public class QueryDslConfig {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public QueryDslConfig() {
//    }
//
//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(entityManager);
//    }
//}