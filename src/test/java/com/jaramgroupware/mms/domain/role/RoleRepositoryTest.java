package com.jaramgroupware.mms.domain.role;

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
class RoleRepositoryTest {

    private final TestUtils testUtils = new TestUtils();

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findRoleById() {
        //given
        Role testGoal = testUtils.getTestRole();
        //when
        Role result = roleRepository.findRoleById(testGoal.getId())
                .orElseThrow(IllegalArgumentException::new);
        //then
        assertEquals(result.toString(),testGoal.toString());
    }

    @Test
    void findAllBy() {
        //given
        List<Role> testGoal = new ArrayList<Role>();
        testGoal.add(testUtils.getTestRole());
        testGoal.add(testUtils.getTestRole2());
        //when
        List<Role> results = roleRepository.findAllBy()
                .orElseThrow(IllegalArgumentException::new);
        //then
        assertThat(testUtils.isListSame(testGoal,results),is(true));
    }

    @Test
    void save() {
        //given
        Role testGoal = testUtils.getTestRole();
        testGoal.setId(null);
        testGoal.setName("ROLE_GUEST");

        //when
        roleRepository.save(testGoal);

        //then
        testGoal.setId(3);
        assertEquals(testGoal.toString(),testEntityManager.find(Role.class,3).toString());
    }

    @Test
    void delete() {
        //given
        Role testGoal = testUtils.getTestRole();

        //when
        roleRepository.delete(testGoal);

        //then
        assertThat(testEntityManager.find(Role.class,testGoal.getId()),is(nullValue()));
    }
}