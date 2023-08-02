package com.jaramgroupware.mms.domain.memberInfo;

import com.jaramgroupware.mms.config.TestQueryDslConfig;
import com.jaramgroupware.mms.domain.major.Major;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.rank.Rank;
import com.jaramgroupware.mms.domain.role.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
class MemberInfoRepositoryTest {

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("existsByStudentID test 1 - 존재하는 학번이 주어지면, true를 반환한다.")
    @Test
    void existsByStudentID() {
        //given
        var targetStudentID = "2021000001";

        //when
        var result = memberInfoRepository.existsByStudentID(targetStudentID);

        //then
        assertTrue(result);
    }

    @DisplayName("existsByStudentID test 2 - 존재하지 않는 학번이 주어지면, true를 반환한다.")
    @Test
    void existsByStudentID2() {
        //given
        var notExistStudentID = "2026000001";

        //when
        var result = memberInfoRepository.existsByStudentID(notExistStudentID);

        //then
        assertFalse(result);
    }

    @DisplayName("existsByPhoneNumber test 1 - 존재하는 휴대폰 번호가 주어지면, true를 반환한다.")
    @Test
    void existsByPhoneNumber() {
        //given
        var targetPhoneNumber = "010-1234-1234";

        //when
        var result = memberInfoRepository.existsByPhoneNumber(targetPhoneNumber);

        //then
        assertTrue(result);
    }

    @DisplayName("existsByPhoneNumber test 2 - 존재하지 않는 휴대폰 번호가 주어지면, false를 반환한다.")
    @Test
    void existsByPhoneNumber2() {
        //given
        var notExistPhoneNumber = "010-9999-9999";

        //when
        var result = memberInfoRepository.existsByPhoneNumber(notExistPhoneNumber);

        //then
        assertFalse(result);
    }

    @DisplayName("findByMember test 1 - 존재하는 Member가 주어지면, 해당 MemberInfo를 반환한다.")
    @Test
    void findByMember() {
        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(5L).name("ROLE_DEV").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();
        //when
        var result = memberInfoRepository.findByMember(target).orElse(null);

        //then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(target, result.getMember());
        assertEquals("010-1234-1234", result.getPhoneNumber());
        assertEquals("2021000001", result.getStudentID());
        assertEquals(37, result.getYear());
        assertEquals(new Rank(3L,"정회원"), result.getRank());
        assertEquals(new Major(4L,"건축학부"), result.getMajor());
        assertEquals(LocalDate.of(2023,7,5), result.getDateOfBirth());
    }

    @DisplayName("findByMember test 2 - 존재하지 않는 Member가 주어지면, null를 반환한다.")
    @Test
    void findByMember2() {
        //given
        var notExistMember = Member.builder()
                .id("1hOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("not")
                .role(Role.builder().id(1L).name("ROLE_GUEST").build())
                .email("not@test.com")
                .build();
        //when
        var result = memberInfoRepository.findByMember(notExistMember).orElse(null);

        //then
        assertNull(result);
    }

    @DisplayName("save test 1 - 정상적인 memberinfo 정보가 주어지면, 해당 MemberInfo를 저장한다.")
    @Test
    void save() {

        entityManager.getEntityManager().createNativeQuery("DELETE FROM MEMBER_INFO AS DI WHERE DI.MEMBER_INFO_PK = :id").setParameter("id",1).executeUpdate();

        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(1L).name("ROLE_GUEST").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();

        var targetMemberInfo = MemberInfo.builder()
                .member(target)
                .phoneNumber("010-1234-1234")
                .studentID("2021000001")
                .year(37)
                .rank(new Rank(3L,"정회원"))
                .major(new Major(4L,"건축학부"))
                .dateOfBirth(LocalDate.of(2023,7,5))
                .build();
        //when
        var result = memberInfoRepository.save(targetMemberInfo);

        //then
        assertNotNull(result);
        assertEquals(7, result.getId());
        assertEquals(target, result.getMember());
        assertEquals("010-1234-1234", result.getPhoneNumber());
        assertEquals("2021000001", result.getStudentID());
        assertEquals(37, result.getYear());
        assertEquals(new Rank(3L,"정회원"), result.getRank());
        assertEquals(new Major(4L,"건축학부"), result.getMajor());
        assertEquals(LocalDate.of(2023,7,5), result.getDateOfBirth());


    }

    @DisplayName("save test 2 - 기존 memberinfo 정보가 주어지면, 해당 MemberInfo를 업데이트 한다.")
    @Test
    void save2() {

        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(5L).name("ROLE_DEV").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();

        var targetMemberInfo = MemberInfo.builder()
                .id(1)
                .member(target)
                .phoneNumber("010-1234-1234")
                .studentID("2021000001")
                .year(36)
                .rank(new Rank(3L,"정회원"))
                .major(new Major(4L,"건축학부"))
                .dateOfBirth(LocalDate.of(2023,7,5))
                .build();
        //when
        var result = memberInfoRepository.save(targetMemberInfo);

        //then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(target, result.getMember());
        assertEquals("010-1234-1234", result.getPhoneNumber());
        assertEquals("2021000001", result.getStudentID());
        assertEquals(36, result.getYear());
        assertEquals(new Rank(3L,"정회원"), result.getRank());
        assertEquals(new Major(4L,"건축학부"), result.getMajor());
        assertEquals(LocalDate.of(2023,7,5), result.getDateOfBirth());


    }

    @DisplayName("saveAndFlush test 1 - 정상적인 memberinfo 정보가 주어지면, 해당 MemberInfo를 저장한다.")
    @Test
    void saveAndFlush() {

        entityManager.getEntityManager().createNativeQuery("DELETE FROM MEMBER_INFO AS DI WHERE DI.MEMBER_INFO_PK = :id").setParameter("id",1).executeUpdate();

        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(1L).name("ROLE_GUEST").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();

        var targetMemberInfo = MemberInfo.builder()
                .member(target)
                .phoneNumber("010-1234-1234")
                .studentID("2021000001")
                .year(37)
                .rank(new Rank(3L,"정회원"))
                .major(new Major(4L,"건축학부"))
                .dateOfBirth(LocalDate.of(2023,7,5))
                .build();
        //when
        var result = memberInfoRepository.save(targetMemberInfo);

        //then
        assertNotNull(result);
        assertEquals(7, result.getId());
        assertEquals(target, result.getMember());
        assertEquals("010-1234-1234", result.getPhoneNumber());
        assertEquals("2021000001", result.getStudentID());
        assertEquals(37, result.getYear());
        assertEquals(new Rank(3L,"정회원"), result.getRank());
        assertEquals(new Major(4L,"건축학부"), result.getMajor());
        assertEquals(LocalDate.of(2023,7,5), result.getDateOfBirth());


    }
}