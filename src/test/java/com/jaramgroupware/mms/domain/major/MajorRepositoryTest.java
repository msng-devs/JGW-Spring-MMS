package com.jaramgroupware.mms.domain.major;


import com.jaramgroupware.mms.config.TestQueryDslConfig;
import com.jaramgroupware.mms.domain.major.queryDsl.QMajorSortKey;
import com.jaramgroupware.mms.utils.querydsl.QueryDslRepositoryUtils;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Import({TestQueryDslConfig.class})
@ExtendWith(SpringExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MajorRepositoryTest {

    @Autowired
    private MajorRepository majorRepository;

    @DisplayName("MajorRepository findById 1 - 올바른 전공 정보 id가 주어지면, 해당 전공 정보를 반환한다.")
    @Test
    void findById() {
        //given

        var targetId = 52L;

        //when

        var result = majorRepository.findById(targetId).orElse(null);

        //then
        assertNotNull(result);
        assertEquals(targetId,result.getId());
        assertEquals("인공지능학과",result.getName());

    }

    @DisplayName("MajorRepository findById 2 - 잘못된 정공 id가 주어지면, null을 반환한다.")
    @Test
    void findById2() {
        //given

        //없는 값
        var targetId = 100L;

        //when

        var result = majorRepository.findById(targetId).orElse(null);

        //then
        assertNull(result);

    }



    @DisplayName("MajorRepository findAll - 올바른 전공명이 주어지면, 해당하는 전공 정보를 반환한다.")
    @Test
    void findAllWithQueryParams() {
        //given
        var pageable = PageRequest.of(0,10, Sort.by("id").descending());
        var params = new LinkedMultiValueMap<String,String>();
        params.add("name","인공지능");

        //when
        var result = majorRepository.findAllWithQueryParams(pageable,params);

        //then
        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals("인공지능학과",result.get(0).getName());
        assertEquals(52L,result.get(0).getId());
    }
}