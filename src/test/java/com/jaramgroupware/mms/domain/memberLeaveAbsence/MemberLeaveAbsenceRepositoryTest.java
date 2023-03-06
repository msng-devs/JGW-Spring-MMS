package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfo;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoSpecification;
import com.jaramgroupware.mms.domain.memberInfo.MemberInfoSpecificationBuilder;
import com.jaramgroupware.mms.domain.role.Role;
import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.format.DateTimeFormatter;
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
    private final MemberLeaveAbsenceSpecificationBuilder memberLeaveAbsenceSpecificationBuilder = new MemberLeaveAbsenceSpecificationBuilder(new ParseByNameBuilder());

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
    void findMemberLeaveAbsenceById() {
        //given
        MemberLeaveAbsence testGoal = testUtils.getTestMemberLeaveAbsence();

        //when
        MemberLeaveAbsence result = memberLeaveAbsenceRepository.findMemberLeaveAbsenceById(testGoal.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertEquals(result.toString(),testGoal.toString());
    }

    @Test
    void findMemberLeaveAbsenceByMember() {
        //given
        MemberLeaveAbsence testGoal = testUtils.getTestMemberLeaveAbsence();

        //when
        MemberLeaveAbsence result = memberLeaveAbsenceRepository.findMemberLeaveAbsenceByMember(testGoal.getMember())
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
        testGoal.setMember(testUtils.getTestMember3());

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
        Set<Integer> testIds = testMemberLeaveAbsences.stream().map(MemberLeaveAbsence::getId).collect(Collectors.toSet());

        //when
        List<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAllByIdIn(testIds);

        //then
        assertThat(res,is(notNullValue()));
        assertTrue(testUtils.isListSame(res,testMemberLeaveAbsences));
    }

    //TODO 테스트 환경 바꿔서 다시 해보기
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
    void findAllWithIntegratedSpec() {
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("status",(testMemberLeaveAbsence.isStatus()) ? "true" : "false" );
        queryParam.add("startExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithIntegratedSpec2() {
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("status",(testMemberLeaveAbsence.isStatus()) ? "true" : "false" );
        queryParam.add("startExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithStatus(){
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("status",(testMemberLeaveAbsence.isStatus()) ? "true" : "false" );
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(2L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(1).toString());
    }

    @Test
    void findAllWithStatus2(){
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence();
        testMemberLeaveAbsence.setStatus(false);

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("status",(testMemberLeaveAbsence.isStatus()) ? "true" : "false" );
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(0L,res.getTotalElements());
    }

    @DisplayName("MemberLeaveAbsence ExpectedDateOfReturnSchool로 검색 - 시작시간과 종료 시간이 주어졌을 경우")
    @Test
    void findAllWithExpectedDateOfReturnSchoolWithStartAndEnd(){
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("startExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("MemberLeaveAbsence ExpectedDateOfReturnSchool로 검색 - 시작시간과 종료 시간이 주어졌을 경우2")
    @Test
    void findAllWithExpectedDateOfReturnSchoolWithStartAndEnd2(){
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("startExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("MemberLeaveAbsence ExpectedDateOfReturnSchool로 검색 - 시작시간이 주어졌을 경우")
    @Test
    void findAllWithExpectedDateOfReturnSchoolWithStart(){
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("startExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(2L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(1).toString());
    }

    @DisplayName("MemberLeaveAbsence ExpectedDateOfReturnSchool로 검색 - 시작시간이 주어졌을 경우2")
    @Test
    void findAllWithExpectedDateOfReturnSchoolWithStart2(){
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("startExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("MemberLeaveAbsence ExpectedDateOfReturnSchool로 검색 - 종료시간이 주어졌을 경우")
    @Test
    void findAllWithExpectedDateOfReturnSchoolWithEnd(){
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("endExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("MemberLeaveAbsence ExpectedDateOfReturnSchool로 검색 - 종료시간이 주어졌을 경우2")
    @Test
    void findAllWithExpectedDateOfReturnSchoolWithEnd2(){
        //given
        MemberLeaveAbsence testMemberLeaveAbsence = testUtils.getTestMemberLeaveAbsence2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("endExpectedDateOfReturnSchool",testMemberLeaveAbsence.getExpectedDateReturnSchool().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberLeaveAbsenceSpecification testSpec = memberLeaveAbsenceSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberLeaveAbsence> res = memberLeaveAbsenceRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(2L,res.getTotalElements());
        assertEquals(testMemberLeaveAbsence.toString(),res.getContent().get(0).toString());
    }

}

