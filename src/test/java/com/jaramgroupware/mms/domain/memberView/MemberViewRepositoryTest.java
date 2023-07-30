package com.jaramgroupware.mms.domain.memberView;

import com.jaramgroupware.mms.config.TestQueryDslConfig;
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
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Import({TestQueryDslConfig.class})
@ExtendWith(SpringExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberViewRepositoryTest {

    @Autowired
    private MemberViewRepository memberViewRepository;

    @DisplayName("findById test 1 - 존재하는 회원의 id가 주어지면, 해당 정보를 리턴한다.")
    @Test
    void findById() {
        //given
        var targetId = "ahOZkVnteYjzrI81hYqysTBGwHWU";
        //when
        var result = memberViewRepository.findById(targetId).orElse(null);
        //then
        assertNotNull(result);
        assertEquals(targetId,result.getUid());
        assertEquals("김개발",result.getName());
        assertEquals("ROLE_DEV",result.getRoleName());
        assertEquals(5L,result.getRole());
        assertTrue(result.getStatus());
        assertEquals("010-1234-1234",result.getCellPhoneNumber());
        assertEquals("2021000001",result.getStudentId());
        assertEquals(37,result.getYear());
        assertEquals(3L,result.getRank());
        assertEquals("정회원",result.getRankName());
        assertEquals(4L,result.getMajor());
        assertEquals("건축학부",result.getMajorName());
        assertEquals(LocalDate.of(2023,7,5),result.getDateOfBirth());
        assertEquals(false,result.getIsLeaveAbsence());


    }

    @DisplayName("findById test 2 - 존재하지 않는 회원의 id가 주어지면, null를 리턴한다.")
    @Test
    void findById2() {
        //given
        var targetId = "1hOZkVnteYjzrI81hYqysTBGwHWU";
        //when
        var result = memberViewRepository.findById(targetId).orElse(null);
        //then
        assertNull(result);


    }
    @DisplayName("findAllWithQueryParams test 1 - 쿼리 파라미터가 주어지면, 해당하는 회원 정보를 리턴한다.")
    @Test
    void findAllWithQueryParams() {
        //given
        var pageable = PageRequest.of(0,10, Sort.by("uid").descending());

        var params = new LinkedMultiValueMap<String,String>();

        params.add("role","5");
        params.add("role","4");

        params.add("rank","3");
        params.add("major","14");

        var exceptResult = MemberView.builder()
                .uid("PFttxmS1wvspzHLkoHXaz4MOLUtE")
                .name("김임원")
                .email("PFttxmS1wvspzHLkoHXaz4MOLUtE@test.com")
                .role(4L)
                .roleName("ROLE_ADMIN")
                .status(true)
                .cellPhoneNumber("010-1111-2222")
                .studentId("2021000005")
                .year(31)
                .rank(3L)
                .rankName("정회원")
                .major(14L)
                .majorName("문화인류학과")
                .dateOfBirth(LocalDate.of(2023,7,5))
                .isLeaveAbsence(false)
                .build();
        //when
        var result = memberViewRepository.findAllWithQueryParams(pageable,params);

        //then
        assertEquals(1,result.size());
        assertEquals(exceptResult.toString(),result.get(0).toString());
    }
}