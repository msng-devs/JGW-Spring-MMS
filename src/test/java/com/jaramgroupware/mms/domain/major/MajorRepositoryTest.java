package com.jaramgroupware.mms.domain.major;

import com.jaramgroupware.mms.TestUtils;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
class MajorRepositoryTest {

    private final TestUtils testUtils = new TestUtils();
    private final MajorSpecificationBuilder majorSpecificationBuilder = new MajorSpecificationBuilder();

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MajorRepository majorRepository;

    @Test
    void findMajorById() {
        //given
        Major testGoal = testUtils.getTestMajor();
        //when
        Major result = majorRepository.findMajorById(1)
                .orElseThrow(IllegalArgumentException::new);
        //then
        assertEquals(result.toString(),testGoal.toString());
    }

    @Test
    void findAllBy() {
        //given
        List<Major> testGoal = new ArrayList<Major>();
        testGoal.add(testUtils.getTestMajor());
        testGoal.add(testUtils.getTestMajor2());
        //when
        List<Major> results = majorRepository.findAllBy()
                .orElseThrow(IllegalArgumentException::new);
        //then
        assertThat(testUtils.isListSame(testGoal,results),is(true));
    }


    @Test
    void findAll(){
        //given
        Major testMajor = testUtils.getTestMajor();
        Major testMajor2 = testUtils.getTestMajor2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("this is not working param",testMajor.getName());

        MajorSpecification testSpec = majorSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Major> res = majorRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(2L,res.getTotalElements());
        assertTrue(testUtils.isListSame(res.getContent(), Arrays.asList(testMajor,testMajor2)));
    }

    @Test
    void findAllWithName(){
        //given
        Major testMajor = testUtils.getTestMajor();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("name",testMajor.getName());

        MajorSpecification testSpec = majorSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Major> res = majorRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMajor.toString(),res.getContent().get(0).toString());
    }

    @Test
    void findAllWithName2(){
        //given
        Major testMajor = testUtils.getTestMajor2();

        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("name",testMajor.getName());

        MajorSpecification testSpec = majorSpecificationBuilder.toSpec(queryParam);

        //when
        Page<Major> res = majorRepository.findAll(testSpec, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC,"id")));

        //then
        assertThat(res,is(notNullValue()));
        assertEquals(1L,res.getTotalElements());
        assertEquals(testMajor.toString(),res.getContent().get(0).toString());
    }

}