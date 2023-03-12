package com.jaramgroupware.mms.domain.member;

import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.utils.parse.ParseByNameBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class MemberRepositoryTest {

    private final TestUtils testUtils = new TestUtils();
    private final MemberSpecificationBuilder memberSpecificationBuilder = new MemberSpecificationBuilder(new ParseByNameBuilder());

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MemberRepository memberRepository;

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
        testGoal.add(testUtils.getTestMember3());
        testGoal.add(testUtils.getTestMember4());
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

    @Test
    void deleteAllByIdInQuery(){
        //given
        Set<String> testIds = new HashSet<>(Arrays.asList(testUtils.getTestMember().getId(),testUtils.getTestMember2().getId())){};

        //when
        memberRepository.deleteAllByIdInQuery(testIds);

        //then
        assertThat(testEntityManager.find(Member.class,testUtils.getTestMember().getId()),is(nullValue()));
        assertThat(testEntityManager.find(Member.class,testUtils.getTestMember2().getId()),is(nullValue()));
    }

    @Test
    void findAllWithIntegratedSpec(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("email",testMember.getEmail());
        queryParam.add("name",testMember.getName());
        queryParam.add("roleID",testMember.getRole().getId().toString());
        queryParam.add("status",(testMember.isStatus()) ? "true" : "false" );
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
        queryParam.add("roleID",testMember.getRole().getId().toString());
        queryParam.add("status",(testMember.isStatus()) ? "true" : "false" );
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
        assertEquals(2L,res.getTotalElements());
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
        assertEquals(2L,res.getTotalElements());
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
        assertEquals(2L,res.getTotalElements());
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
        assertEquals(2L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithStatus(){
        //given
        Member testMember = testUtils.getTestMember();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","true");
        queryParam.add("status",(testMember.isStatus()) ? "true" : "false" );
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithStatus2(){
        //given
        Member testMember = testUtils.getTestMember2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("includeGuest","false");
        queryParam.add("status",(testMember.isStatus()) ? "true" : "false" );
        MemberSpecification testSpec = memberSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Member> res = memberRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMember.toString(),res.getContent().get(0).toString());
    }
    
    @Test
    void existsByEmail() {
        //given
        Member testMember = testUtils.getTestMember();

        //when
        boolean result = memberRepository.existsByEmail(testMember.getEmail());

        //then
        assertTrue(result);
    }

    @Test
    void existsByEmail2() {
        //given
        Member testMember = testUtils.getTestMember();
        testMember.setEmail("test@test.com");

        //when
        boolean result = memberRepository.existsByEmail(testMember.getEmail());

        //then
        assertFalse(result);
    }
}