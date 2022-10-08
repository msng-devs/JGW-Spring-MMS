package com.jaramgroupware.mms.domain.member;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.rank.Rank;
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
class MemberRepositoryTest {

    private final TestUtils testUtils = new TestUtils();
    private final MemberSpecificationBuilder memberSpecificationBuilder = new MemberSpecificationBuilder(new ParseByNameBuilder());

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findMemberById() {
        //given
        Member testGoal = testUtils.getTestMember();
        //when
        Member result = memberRepository.findMemberById(testGoal.getId())
                .orElseThrow(IllegalArgumentException::new);
        //then
        assertEquals(result.toString(),testGoal.toString());
    }

    @Test
    void findAllBy() {
        //given
        List<Member> testGoal = new ArrayList<Member>();
        testGoal.add(testUtils.getTestMember());
        testGoal.add(testUtils.getTestMember2());
        //when
        List<Member> results = memberRepository.findAllBy()
                .orElseThrow(IllegalArgumentException::new);
        //then
        assertThat(testUtils.isListSame(testGoal,results),is(true));
    }

    @Test
    void save() {
        //given
        Member testGoal = testUtils.getTestMember();
        testGoal.setId("ThisisnotrealuiDDOY0UKNOWH0S");

        //when
        memberRepository.save(testGoal);

        //then
        assertEquals(testGoal.toString(),testEntityManager.find(Member.class,testGoal.getId()).toString());
    }

    @Test
    void delete() {
        //given
        Member testGoal = testUtils.getTestMember();

        //when
        memberRepository.delete(testGoal);
        //then
        assertThat(testEntityManager.find(Member.class,testGoal.getId()),is(nullValue()));
    }

    @Test
    void findAllByIdIn(){
        //given
        List<Member> testMembers = Arrays.asList(testUtils.getTestMember(),testUtils.getTestMember2());
        Set<String> testIds = testMembers.stream().map(Member::getId).collect(Collectors.toSet());

        //when
        List<Member> res = memberRepository.findAllByIdIn(testIds);

        //then
        assertThat(res,is(notNullValue()));
        assertTrue(testUtils.isListSame(res,testMembers));
    }
//TODO H2 에서만 생기는 오류 고치기

//    @Test
//    void deleteAllByIdInQuery(){
//        //given
//        Set<String> testIds = new HashSet<>(Arrays.asList(testUtils.getTestMember2().toId(),testUtils.getTestMember().toId())){};
//
//        //when
//        memberRepository.deleteAllByIdInQuery(testIds);
//
//        //then
//        assertThat(testEntityManager.find(Member.class,testUtils.getTestMember().toId()),is(nullValue()));
//        assertThat(testEntityManager.find(Member.class,testUtils.getTestMember2().toId()),is(nullValue()));
//    }
    @Test
    void findTargetMember(){
        //given
        //testMember2의 leaveAbsence가 true이기 때문에, testMember만 검색되야함
        List<Member> testMembers = Arrays.asList(testUtils.getTestMember2(),testUtils.getTestMember());
        Set<Rank> testRanks = testMembers.stream().map(Member::getRank).collect(Collectors.toSet());

        //when
        List<Member> res = memberRepository.findTargetMember(testRanks).orElseThrow();

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(res.size(),1);
        assertEquals(res.get(0).toString(),testUtils.getTestMember().toString());
    }

    @Test
    void findTargetMember2(){
        //given
        //1번 테스트와 다르게, 둘다 leaveabesence가 false이므로, 둘다 검색되야함
        Member testMember = testEntityManager.find(Member.class,testUtils.getTestMember2().getId());
        testMember.setLeaveAbsence(false);
        testEntityManager.persistAndFlush(testMember);

        List<Member> testMembers = Arrays.asList(testMember,testUtils.getTestMember());
        Set<Rank> testRanks = testMembers.stream().map(Member::getRank).collect(Collectors.toSet());

        //when
        List<Member> res = memberRepository.findTargetMember(testRanks).orElseThrow();

        //then
        assertThat(res,is(notNullValue()));
        assertTrue(testUtils.isListSame(res,testMembers));
    }

    @Test
    void bulkInsert(){
        //given
        Member testGoal = testUtils.getTestMember();
        testGoal.setId("Thisisn0trealuiDDOY0UKNOWH0S");
        testGoal.setStudentID("2021000001");

        Member testGoal2 = testUtils.getTestMember2();
        testGoal2.setId("thisisn0trealui1DO50UKNOWH0s");
        testGoal2.setStudentID("2021000009");

        List<Member> testMembers = new ArrayList<>();
        testMembers.add(testGoal);
        testMembers.add(testGoal2);

        //when
        memberRepository.bulkInsert(testMembers, testUtils.testUid);

        //then
        assertEquals(testEntityManager.find(Member.class,testGoal.getId()).toString(),testGoal.toString());
        assertEquals(testEntityManager.find(Member.class,testGoal2.getId()).toString(),testGoal2.toString());
    }

    @Test
    void bulkUpdate(){
        //given
        Member testGoal = testUtils.getTestMember();
        testGoal.setPhoneNumber("01011111111");

        Member testGoal2 = testUtils.getTestMember2();
        testGoal2.setPhoneNumber("01011111111");

        List<Member> testMembers = new ArrayList<>();
        testMembers.add(testGoal);
        testMembers.add(testGoal2);

        //when
        memberRepository.bulkUpdate(testMembers, testUtils.testUid);

        //then
        assertEquals(testEntityManager.find(Member.class,testGoal.getId()).toString(),testGoal.toString());
        assertEquals(testEntityManager.find(Member.class,testGoal2.getId()).toString(),testGoal2.toString());
    }

    @Test
    void findAllWithIntegratedSpec(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("email",testMember.getEmail());
        queryParam.add("name",testMember.getName());
        queryParam.add("phoneNumber",testMember.getPhoneNumber());
        queryParam.add("studentID",testMember.getStudentID());
        queryParam.add("majorID",testMember.getMajor().getId().toString());
        queryParam.add("rankID",testMember.getRank().getId().toString());
        queryParam.add("roleID",testMember.getRole().getId().toString());
        queryParam.add("year",testMember.getYear().toString());
        queryParam.add("leaveAbsence",(testMember.isLeaveAbsence()) ? "true" : "false" );
        queryParam.add("modifiedBy",testMember.getCreateBy());
        queryParam.add("createBy",testMember.getCreateBy());
        queryParam.add("startCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("startModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("startDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }


    @Test
    void findAllWithIntegratedSpec2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("email",testMember.getEmail());
        queryParam.add("name",testMember.getName());
        queryParam.add("phoneNumber",testMember.getPhoneNumber());
        queryParam.add("studentID",testMember.getStudentID());
        queryParam.add("majorID",testMember.getMajor().getId().toString());
        queryParam.add("rankID",testMember.getRank().getId().toString());
        queryParam.add("roleID",testMember.getRole().getId().toString());
        queryParam.add("year",testMember.getYear().toString());
        queryParam.add("leaveAbsence",(testMember.isLeaveAbsence()) ? "true" : "false" );
        queryParam.add("modifiedBy",testMember.getCreateBy());
        queryParam.add("createBy",testMember.getCreateBy());
        queryParam.add("startCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("startModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("startDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithIncludeGuest(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithNoIncludeGuest(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithEmail(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("email",testMember.getEmail());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithEmail2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("email",testMember.getEmail());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithName(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("name",testMember.getName());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithName2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("name",testMember.getName());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithPhoneNumber(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("phoneNumber",testMember.getPhoneNumber());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithPhoneNumber2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("phoneNumber",testMember.getPhoneNumber());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithStudentID(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("studentID",testMember.getStudentID());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithStudentID2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("studentID",testMember.getStudentID());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithMajorID(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("majorID",testMember.getMajor().getId().toString());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithMajorID2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("majorID",testMember.getMajor().getId().toString());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithRankID(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("rankID",testMember.getRank().getId().toString());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithRankID2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("rankID",testMember.getRank().getId().toString());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithRoleID(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("roleID",testMember.getRole().getId().toString());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithRoleID2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("roleID",testMember.getRole().getId().toString());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithYear(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("year",testMember.getYear().toString());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithYear2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("year",testMember.getYear().toString());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithLeaveAbsence(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("leaveAbsence",(testMember.isLeaveAbsence()) ? "true" : "false" );
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithLeaveAbsence2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("leaveAbsence",(testMember.isLeaveAbsence()) ? "true" : "false" );
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithModifiedBy(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("modifiedBy",testMember.getCreateBy());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithModifiedBy2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("modifiedBy",testMember.getCreateBy());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithCreatedBy(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("createBy",testMember.getCreateBy());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithCreatedBy2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("createBy",testMember.getCreateBy());
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 시작시간과 종료 시간이 주어졌을 경우")
    @Test
    void findAllWithCreatedDateTimeWithStartAndEnd(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 시작시간과 종료 시간이 주어졌을 경우2")
    @Test
    void findAllWithCreatedDateTimeWithStartAndEnd2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 시작시간이 주어졌을 경우")
    @Test
    void findAllWithCreatedDateTimeWithStart(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 시작시간이 주어졌을 경우2")
    @Test
    void findAllWithCreatedDateTimeWithStart2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 종료시간이 주어졌을 경우")
    @Test
    void findAllWithCreatedDateTimeWithEnd(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("endCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 종료시간이 주어졌을 경우2")
    @Test
    void findAllWithCreatedDateTimeWithEnd2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("endCreatedDateTime",testMember.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 시작시간과 종료 시간이 주어졌을 경우")
    @Test
    void findAllWithModifiedDateTimeTimeWithStartAndEnd(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 시작시간과 종료 시간이 주어졌을 경우2")
    @Test
    void findAllWithModifiedDateTimeWithStartAndEnd2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 시작시간이 주어졌을 경우")
    @Test
    void findAllWithModifiedDateTimeWithStart(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 시작시간이 주어졌을 경우2")
    @Test
    void findAllWithModifiedDateTimeWithStart2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 종료시간이 주어졌을 경우")
    @Test
    void findAllWithModifiedDateTimeWithEnd(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("endModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 종료시간이 주어졌을 경우2")
    @Test
    void findAllWithModifiedDateTimeWithEnd2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("endModifiedDateTime",testMember.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 시작시간과 종료 시간이 주어졌을 경우")
    @Test
    void findAllWithDateOfBirthWithStartAndEnd(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 시작시간과 종료 시간이 주어졌을 경우2")
    @Test
    void findAllWithDateOfBirthWithStartAndEnd2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 시작시간이 주어졌을 경우")
    @Test
    void findAllWithDateOfBirthWithStart(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 시작시간이 주어졌을 경우2")
    @Test
    void findAllWithDateOfBirthWithStart2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 종료시간이 주어졌을 경우")
    @Test
    void findAllWithDateOfBirthWithEnd(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("endDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 종료시간이 주어졌을 경우2")
    @Test
    void findAllWithDateOfBirthWithEnd2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("endDateOfBirth",testMember.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }
}