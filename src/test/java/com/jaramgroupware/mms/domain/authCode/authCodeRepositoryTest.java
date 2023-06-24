package com.jaramgroupware.mms.domain.authCode;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
public class authCodeRepositoryTest {

    private final TestUtils testUtils = new TestUtils();

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AuthCodeRepository authCodeRepository;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAuthCodeById() {
        //given
        AuthCode testGoal = testUtils.getTestAuthCode();

        //when
        AuthCode result = authCodeRepository.findById(testGoal.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertEquals(result.toString(),testGoal.toString());
    }

    @Test
    void save() {
        //given
        AuthCode testGoal = testUtils.getTestAuthCode();

        //when
        authCodeRepository.save(testGoal);

        //then
        assertEquals(testGoal.toString(),testEntityManager.find(AuthCode.class,testGoal.getId()).toString());
    }

    @Test
    void delete() {
        //given
        AuthCode testGoal = testUtils.getTestAuthCode();

        //when
        authCodeRepository.delete(testGoal);

        //then
        assertThat(testEntityManager.find(AuthCode.class,testGoal.getId()),is(nullValue()));
    }

}
