package com.jaramgroupware.mms.domain.rank;

import com.jaramgroupware.mms.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SqlGroup({
        @Sql(scripts = "classpath:tableBuild.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testDataSet.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
class RankRepositoryTest {

    private final TestUtils testUtils = new TestUtils();

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RankRepository rankRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findRankById() {
        //given
        Rank testGoal = testUtils.getTestRank();
        //when
        Rank result = rankRepository.findRankById(testGoal.getId())
                .orElseThrow(IllegalArgumentException::new);
        //then
        assertEquals(result.toString(),testGoal.toString());
    }

    @Test
    void findAllBy() {
        //given
        List<Rank> testGoal = new ArrayList<Rank>();
        testGoal.add(testUtils.getTestRank());
        testGoal.add(testUtils.getTestRank2());
        //when
        List<Rank> results = rankRepository.findAllBy()
                .orElseThrow(IllegalArgumentException::new);
        //then
        assertThat(testUtils.isListSame(testGoal,results),is(true));
    }

    @Test
    void save() {
        //given
        Rank testGoal = testUtils.getTestRank();
        testGoal.setId(null);
        testGoal.setName("수습회원");

        //when
        rankRepository.save(testGoal);

        //then
        testGoal.setId(3);
        assertEquals(testGoal.toString(),testEntityManager.find(Rank.class,3).toString());
    }

    @Test
    void delete() {
        //given
        Rank testGoal = testUtils.getTestRank();

        //when
        rankRepository.delete(testGoal);

        //then
        assertThat(testEntityManager.find(Rank.class,testGoal.getId()),is(nullValue()));
    }
}