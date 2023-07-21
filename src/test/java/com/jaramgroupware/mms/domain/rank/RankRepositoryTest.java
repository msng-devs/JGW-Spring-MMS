package com.jaramgroupware.mms.domain.rank;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RankRepositoryTest {

    @Autowired
    private RankRepository rankRepository;

    @DisplayName("findById test 1 - 존재하는 rank의 id가 주어지면, 해당 정보를 리턴한다.")
    @Test
    void findById(){
        //given
        var target = 1L;
        //when
        var rank = rankRepository.findById(target).orElse(null);
        //then
        assertNotNull(rank);
        assertEquals(target,rank.getId());
        assertEquals("게스트",rank.getName());
    }

    @DisplayName("findById test 3 - 존재하지 않는 rank의 id가 주어지면, 해당 정보를 리턴한다.")
    @Test
    void findById3(){
        //given
        var target = 1000L;
        //when
        var rank = rankRepository.findById(target).orElse(null);
        //then
        assertNull(rank);
    }

    @Test
    void findAllBy() {
    }
}