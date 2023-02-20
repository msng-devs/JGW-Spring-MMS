package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.role.Role;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SqlGroup({
        @Sql(scripts = "classpath:tableBuild.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testDataSet.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
public class MemberLeaveAbsenceRepositoryTest {

    private final TestUtils testUtils = new TestUtils();

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findMemberAbsenceById() {
        //given
        MemberLeaveAbsence testGoal = testUtils.getTestMemberLeaveAbsence();

        //when
        MemberLeaveAbsence result = memberLeaveAbsenceRepository.findMemberLeaveAbsenceById(testGoal.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertEquals(result.toString(),testGoal.toString());
    }

    @Test
    void findAllBy() {
        //given
        List<MemberLeaveAbsence> testGoal = new ArrayList<>();
        testGoal.add(testUtils.getTestMemberLeaveAbsence());
        testGoal.add(testUtils.getTestMemberLeaveAbsence2());

        //when
        List<MemberLeaveAbsence> results = memberLeaveAbsenceRepository.findAllBy()
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(testUtils.isListSame(testGoal,results),is(true));
    }

    @Test
    void save() {
        //given
        MemberLeaveAbsence testGoal = testUtils.getTestMemberLeaveAbsence();
        testGoal.setId("AASDFGHJKLZXCVBNMQWERTYUIOPS");
        testGoal.setMember(Member.builder()
                .id("AASDFGHJKLZXCVBNMQWERTYUIOPS")
                .name("이테스트")
                .email("lee@test.com")
                .status(false)
                .role(Role.builder()
                        .id(1)
                        .name("ROLE_ADMIN")
                        .build())
                .build());

        //when
        memberLeaveAbsenceRepository.save(testGoal);

        //then
        assertEquals(testGoal.toString(),testEntityManager.find(MemberLeaveAbsence.class,testGoal.getId()).toString());
    }

    @Test
    void delete() {
        //given
        MemberLeaveAbsence testGoal = testUtils.getTestMemberLeaveAbsence();

        //when
        memberLeaveAbsenceRepository.delete(testGoal);

        //then
        assertThat(testEntityManager.find(MemberLeaveAbsence.class,testGoal.getId()),is(nullValue()));
    }

    @Test
    void findAllByIdIn(){
        //given
        List<MemberLeaveAbsence> testMemberLeaveAbsences = Arrays.asList(testUtils.getTestMemberLeaveAbsence(),testUtils.getTestMemberLeaveAbsence2());
        Set<String> testIds = testMemberLeaveAbsences.stream().map(MemberLeaveAbsence::getId).collect(Collectors.toSet());

        //when
        List<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAllByIdIn(testIds);

        //then
        assertThat(res,is(notNullValue()));
        assertTrue(testUtils.isListSame(res,testMemberLeaveAbsences));
    }

//    @Test
//    void deleteAllByIdInQuery(){
//        //given
//        Set<String> testIds = new HashSet<>(Arrays.asList(testUtils.getTestMemberLeaveAbsence().getId(),testUtils.getTestMemberLeaveAbsence2().getId())){};
//
//        //when
//        memberLeaveAbsenceRepository.deleteAllByIdInQuery(testIds);
//
//        //then
//        assertThat(testEntityManager.find(MemberLeaveAbsence.class,testUtils.getTestMemberLeaveAbsence().getId()),is(nullValue()));
//        assertThat(testEntityManager.find(MemberLeaveAbsence.class,testUtils.getTestMemberLeaveAbsence2().getId()),is(nullValue()));
//    }

    @Test
    void bulkInsert(){
        //given
        MemberLeaveAbsence testGoal = testUtils.getTestMemberLeaveAbsence();
        testGoal.setId(testUtils.getTestMember3().getId());
        testGoal.setMember(testUtils.getTestMember3());

        MemberLeaveAbsence testGoal2 = testUtils.getTestMemberLeaveAbsence2();
        testGoal2.setId(testUtils.getTestMember4().getId());
        testGoal2.setMember(testUtils.getTestMember4());

        List<MemberLeaveAbsence> testMemberLeaveAbsences = new ArrayList<>();
        testMemberLeaveAbsences.add(testGoal);
        testMemberLeaveAbsences.add(testGoal2);

        //when
        memberLeaveAbsenceRepository.bulkInsert(testMemberLeaveAbsences);

        //then
        assertEquals(testEntityManager.find(MemberLeaveAbsence.class,testGoal.getId()).toString(),testGoal.toString());
        assertEquals(testEntityManager.find(MemberLeaveAbsence.class,testGoal2.getId()).toString(),testGoal2.toString());
    }

}

