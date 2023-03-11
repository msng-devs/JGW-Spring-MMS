package com.jaramgroupware.mms.domain.memberInfo;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.member.Member;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
public class MemberInfoRepositoryTest {

    private final TestUtils testUtils = new TestUtils();
    private final MemberInfoSpecificationBuilder memberInfoSpecificationBuilder = new MemberInfoSpecificationBuilder(new ParseByNameBuilder());

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findMemberInfoById() {
        //given
        MemberInfo testGoal = testUtils.getTestMemberInfo();

        //when
        MemberInfo result = memberInfoRepository.findMemberInfoById(testGoal.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertEquals(result.toString(),testGoal.toString());
    }

    @Test
    void findMemberInfoByMember() {
        //given
        MemberInfo testGoal = testUtils.getTestMemberInfo();

        //when
        MemberInfo result = memberInfoRepository.findMemberInfoByMember(testGoal.getMember())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertEquals(result.toString(),testGoal.toString());

    }

    @Test
    void existsByStudentID() {
        //given
        MemberInfo testGoal = testUtils.getTestMemberInfo();

        //when
        boolean result = memberInfoRepository.existsByStudentID(testGoal.getStudentID());

        //then
        assertTrue(result);
    }

    @Test
    void existsByStudentID2() {
        //given
        MemberInfo testGoal = testUtils.getTestMemberInfo();
        testGoal.setStudentID("2023000000");

        //when
        boolean result = memberInfoRepository.existsByStudentID(testGoal.getStudentID());

        //then
        assertFalse(result);
    }

    @Test
    void findAllBy() {
        //given
        List<MemberInfo> testGoal = new ArrayList<>();
        testGoal.add(testUtils.getTestMemberInfo());
        testGoal.add(testUtils.getTestMemberInfo2());

        //when
        List<MemberInfo> results = memberInfoRepository.findAllBy()
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(testUtils.isListSame(testGoal,results),is(true));
    }

    @Test
    void save() {
        //given
        MemberInfo testGoal = testUtils.getTestMemberInfo();

        //when
        memberInfoRepository.save(testGoal);

        //then
        assertEquals(testGoal.toString(),testEntityManager.find(MemberInfo.class,testGoal.getId()).toString());
    }

    @Test
    void delete() {
        //given
        MemberInfo testGoal = testUtils.getTestMemberInfo();

        //when
        memberInfoRepository.delete(testGoal);

        //then
        assertThat(testEntityManager.find(MemberInfo.class,testGoal.getId()),is(nullValue()));
    }

    @Test
    void findAllByIdIn(){
        //given
        List<MemberInfo> testMemberInfos = Arrays.asList(testUtils.getTestMemberInfo(),testUtils.getTestMemberInfo2());
        Set<Integer> testIds = testMemberInfos.stream().map(MemberInfo::getId).collect(Collectors.toSet());

        //when
        List<MemberInfo> res = memberInfoRepository.findAllByIdIn(testIds);

        //then
        assertThat(res,is(notNullValue()));
        assertTrue(testUtils.isListSame(res,testMemberInfos));
    }

    @Test
    void deleteAllByIdInQuery(){
        //given
        Set<Integer> testIds = new HashSet<>(Arrays.asList(testUtils.getTestMemberInfo().getId(),testUtils.getTestMemberInfo2().getId())){};

        //when
        memberInfoRepository.deleteAllByIdInQuery(testIds);

        //then
        assertThat(testEntityManager.find(MemberInfo.class,testUtils.getTestMemberInfo().getId()),is(nullValue()));
        assertThat(testEntityManager.find(MemberInfo.class,testUtils.getTestMemberInfo2().getId()),is(nullValue()));
    }

    @Test
    void findAllWithIntegratedSpec() {
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("memberID",testMemberInfo.getMember().getId());
        queryParam.add("phoneNumber",testMemberInfo.getPhoneNumber());
        queryParam.add("studentID",testMemberInfo.getStudentID());
        queryParam.add("majorID",testMemberInfo.getMajor().getId().toString());
        queryParam.add("rankID",testMemberInfo.getRank().getId().toString());
        queryParam.add("year",testMemberInfo.getYear().toString());
        queryParam.add("modifiedBy",testMemberInfo.getCreateBy());
        queryParam.add("createBy",testMemberInfo.getCreateBy());
        queryParam.add("startCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("startModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("startDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithIntegratedSpec2() {
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("phoneNumber",testMemberInfo.getPhoneNumber());
        queryParam.add("studentID",testMemberInfo.getStudentID());
        queryParam.add("majorID",testMemberInfo.getMajor().getId().toString());
        queryParam.add("rankID",testMemberInfo.getRank().getId().toString());
        queryParam.add("year",testMemberInfo.getYear().toString());
        queryParam.add("modifiedBy",testMemberInfo.getCreateBy());
        queryParam.add("createBy",testMemberInfo.getCreateBy());
        queryParam.add("startCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("startModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("startDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithPhoneNumber(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("phoneNumber",testMemberInfo.getPhoneNumber());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithPhoneNumber2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("phoneNumber",testMemberInfo.getPhoneNumber());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithStudentID(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("studentID",testMemberInfo.getStudentID());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithStudentID2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("studentID",testMemberInfo.getStudentID());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithMajorID(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("majorID",testMemberInfo.getMajor().getId().toString());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithMajorID2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("majorID",testMemberInfo.getMajor().getId().toString());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithRankID(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("rankID",testMemberInfo.getRank().getId().toString());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithRankID2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("rankID",testMemberInfo.getRank().getId().toString());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithYear(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("year",testMemberInfo.getYear().toString());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithYear2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("year",testMemberInfo.getYear().toString());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithModifiedBy(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("modifiedBy",testMemberInfo.getCreateBy());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithModifiedBy2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("modifiedBy",testMemberInfo.getCreateBy());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithCreatedBy(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("createBy",testMemberInfo.getCreateBy());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithCreatedBy2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("createBy",testMemberInfo.getCreateBy());
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 시작시간과 종료 시간이 주어졌을 경우")
    @Test
    void findAllWithCreatedDateTimeWithStartAndEnd(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 시작시간과 종료 시간이 주어졌을 경우2")
    @Test
    void findAllWithCreatedDateTimeWithStartAndEnd2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 시작시간이 주어졌을 경우")
    @Test
    void findAllWithCreatedDateTimeWithStart(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 시작시간이 주어졌을 경우2")
    @Test
    void findAllWithCreatedDateTimeWithStart2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 종료시간이 주어졌을 경우")
    @Test
    void findAllWithCreatedDateTimeWithEnd(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("endCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member CreatedDateTime으로 검색 - 종료시간이 주어졌을 경우2")
    @Test
    void findAllWithCreatedDateTimeWithEnd2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("endCreatedDateTime",testMemberInfo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 시작시간과 종료 시간이 주어졌을 경우")
    @Test
    void findAllWithModifiedDateTimeTimeWithStartAndEnd(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 시작시간과 종료 시간이 주어졌을 경우2")
    @Test
    void findAllWithModifiedDateTimeWithStartAndEnd2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        queryParam.add("endCreatedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 시작시간이 주어졌을 경우")
    @Test
    void findAllWithModifiedDateTimeWithStart(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 시작시간이 주어졌을 경우2")
    @Test
    void findAllWithModifiedDateTimeWithStart2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 종료시간이 주어졌을 경우")
    @Test
    void findAllWithModifiedDateTimeWithEnd(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("endModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member ModifiedDateTime으로 검색 - 종료시간이 주어졌을 경우2")
    @Test
    void findAllWithModifiedDateTimeWithEnd2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("endModifiedDateTime",testMemberInfo.getModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 시작시간과 종료 시간이 주어졌을 경우")
    @Test
    void findAllWithDateOfBirthWithStartAndEnd(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 시작시간과 종료 시간이 주어졌을 경우2")
    @Test
    void findAllWithDateOfBirthWithStartAndEnd2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        queryParam.add("endDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 시작시간이 주어졌을 경우")
    @Test
    void findAllWithDateOfBirthWithStart(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("startDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 시작시간이 주어졌을 경우2")
    @Test
    void findAllWithDateOfBirthWithStart2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("startDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 종료시간이 주어졌을 경우")
    @Test
    void findAllWithDateOfBirthWithEnd(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("endDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

    @DisplayName("Member DateOfBirth으로 검색 - 종료시간이 주어졌을 경우2")
    @Test
    void findAllWithDateOfBirthWithEnd2(){
        //given
        MemberInfo testMemberInfo = testUtils.getTestMemberInfo();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("endDateOfBirth",testMemberInfo.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MemberInfoSpecification testSpec = memberInfoSpecificationBuilder.toSpec(queryParam);

        //when
        Page<MemberInfo> res = memberInfoRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMemberInfo.toString(),res.getContent().get(0).toString());
    }

}


