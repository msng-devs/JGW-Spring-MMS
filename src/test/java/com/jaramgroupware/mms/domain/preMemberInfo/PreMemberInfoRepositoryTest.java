package com.jaramgroupware.mms.domain.preMemberInfo;

import com.jaramgroupware.mms.config.TestQueryDslConfig;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.registerCode.RegisterCode;
import com.jaramgroupware.mms.domain.role.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Import({TestQueryDslConfig.class})
@ExtendWith(SpringExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PreMemberInfoRepositoryTest {

    @Autowired
    private PreMemberInfoRepository preMemberInfoRepository;

    @DisplayName("findById test 1 - 회원가입 코드가 존재하지 않는 회원의 id가 주어지면, 해당 정보를 리턴한다.")
    @Test
    void findById(){
        //given
        var target = 1L;

        //when

        var preMemberInfo = preMemberInfoRepository.findById(target).orElse(null);

        //then
        assertNotNull(preMemberInfo);
        assertEquals(target,preMemberInfo.getId());
        assertEquals("신규휴학",preMemberInfo.getName());
        assertEquals("2021000003",preMemberInfo.getStudentId());
        assertEquals(37,preMemberInfo.getYear());
        assertEquals(new Rank(5L,"OB"),preMemberInfo.getRank());
        assertEquals(new Major(5L,"산업경영공학과"),preMemberInfo.getMajor());
        assertEquals(new Role(3L,"ROLE_USER1"),preMemberInfo.getRole());
        assertEquals(LocalDate.of(2023,7,6),preMemberInfo.getExpectedDateReturnSchool());


    }

    @DisplayName("findById test 2 - 회원가입 코드가 존재하는 회원의 id가 주어지면, 해당 정보를 리턴한다.")
    @Test
    void findById2(){
        //given
        var target = 2L;

        //when

        var preMemberInfo = preMemberInfoRepository.findById(target).orElse(null);

        //then
        assertNotNull(preMemberInfo);
        assertEquals(target,preMemberInfo.getId());
        assertEquals("일반",preMemberInfo.getName());
        assertEquals("2021000004",preMemberInfo.getStudentId());
        assertEquals(36,preMemberInfo.getYear());
        assertEquals(new Rank(4L,"준OB"),preMemberInfo.getRank());
        assertEquals(new Major(4L,"건축학부"),preMemberInfo.getMajor());
        assertEquals(new Role(2L,"ROLE_USER0"),preMemberInfo.getRole());
        assertNull(preMemberInfo.getExpectedDateReturnSchool());
        assertEquals("b691bb08-6d01-4aef-bf9e-54bb4a283496",preMemberInfo.getRegisterCode().getCode());
        assertEquals("KPzdvnpBicaIldPU8NRWFZMog0MP",preMemberInfo.getRegisterCode().getCreateBy());
        assertEquals(LocalDate.of(2023,7,6),preMemberInfo.getRegisterCode().getExpiredAt());

    }

    @DisplayName("findById test 3 - 존재하지 않는 회원의 id가 주어지면, null를 리턴한다.")
    @Test
    void findById3(){
        //given
        var target = 10L;

        //when

        var preMemberInfo = preMemberInfoRepository.findById(target).orElse(null);

        //then
        assertNull(preMemberInfo);

    }

    @Test
    void findAllWithQueryParams(){
        //given
        var queryParams = new LinkedMultiValueMap<String,String>();
        queryParams.add("rank","5");
        queryParams.add("rank","4");
        queryParams.add("major","5");
        var pageable = PageRequest.of(0,10, Sort.by("id").descending());

        var exceptedPreMemberInfo = PreMemberInfo.builder()
                .id(1L)
                .studentId("2021000003")
                .year(37)
                .role(new Role(3L,"ROLE_USER1"))
                .name("신규휴학")
                .major(new Major(5L,"산업경영공학과"))
                .rank(new Rank(5L,"OB"))
                .expectedDateReturnSchool(LocalDate.of(2023,7,6))
                .build();

        //when
        var result = preMemberInfoRepository.findAllWithQueryParams(pageable,queryParams);

        //then
        assertEquals(1,result.size());
        assertEquals(exceptedPreMemberInfo.toString(),result.get(0).toString());
    }

    @Test
    void save(){
        //given
        var target = PreMemberInfo.builder()
                .studentId("2021000005")
                .year(37)
                .role(new Role(3L,"ROLE_USER1"))
                .name("신규휴학")
                .major(new Major(5L,"산업경영공학과"))
                .rank(new Rank(5L,"OB"))
                .expectedDateReturnSchool(LocalDate.of(2023,7,6))
                .build();

        //when
        var result = preMemberInfoRepository.save(target);

        //then
        assertEquals(target.toString(),result.toString());

    }

    @Test
    void existsByStudentId() {
        //given
        var target = "2021000003";
        //when
        var result = preMemberInfoRepository.existsByStudentId(target);
        //then
        assertTrue(result);

    }

    @Test
    void delete(){
        //given
        var target = PreMemberInfo.builder()
                .id(1L)
                .studentId("2021000003")
                .year(37)
                .role(new Role(3L,"ROLE_USER1"))
                .name("신규휴학")
                .major(new Major(5L,"산업경영공학과"))
                .rank(new Rank(5L,"OB"))
                .expectedDateReturnSchool(LocalDate.of(2023,7,6))
                .build();
        //when
        preMemberInfoRepository.delete(target);
        //then

    }
}