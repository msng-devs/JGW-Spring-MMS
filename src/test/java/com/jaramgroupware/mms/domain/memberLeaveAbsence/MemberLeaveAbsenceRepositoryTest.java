package com.jaramgroupware.mms.domain.memberLeaveAbsence;

import com.jaramgroupware.mms.config.TestQueryDslConfig;
import com.jaramgroupware.mms.domain.member.Member;
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
class MemberLeaveAbsenceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberLeaveAbsenceRepository memberLeaveAbsenceRepository;

    @DisplayName("findByMember test 1 - 존재하는 member가 주어지면, 해당 멤버의 휴학 정보를 반환한다.")
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
        var result = memberLeaveAbsenceRepository.findByMember(target).orElse(null);

        //then
        assertNotNull(result);
        assertEquals(1,result.getId());
        assertFalse(result.isStatus());
        assertNull(result.getExpectedDateReturnSchool());
        assertEquals(target,result.getMember());

    }

    @DisplayName("findByMember test 2 - 존재하지 않는 member가 주어지면, null를 반환한다.")
    @Test
    void findByMember2() {
        //given
        var target = Member.builder()
                .id("1hOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("not exist member")
                .role(Role.builder().id(5L).name("ROLE_DEV").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();
        //when
        var result = memberLeaveAbsenceRepository.findByMember(target).orElse(null);

        //then
        assertNull(result);

    }

    @DisplayName("save test 1 - 정상적인 데이터가 주어지면, DB에 저장한다.")
    @Test
    void save(){
        //given
        entityManager.getEntityManager().createNativeQuery("DELETE FROM MEMBER_LEAVE_ABSENCE AS MLA WHERE MLA.MEMBER_LEAVE_ABSENCE_PK = :id").setParameter("id",1).executeUpdate();

        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(5L).name("ROLE_DEV").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();

        var memberLeaveAbsence = MemberLeaveAbsence.builder()
                .member(target)
                .status(false)
                .expectedDateReturnSchool(null)
                .build();

        //when

        var result = memberLeaveAbsenceRepository.save(memberLeaveAbsence);

        //then
        assertNotNull(result);
        assertEquals(7,result.getId());
        assertFalse(result.isStatus());
        assertNull(result.getExpectedDateReturnSchool());
        assertEquals(target,result.getMember());
    }

    @DisplayName("save test 2 - 이미 존재하는 데이터가 주어지면, 업데이트 한다.")
    @Test
    void save2(){
        //given

        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(5L).name("ROLE_DEV").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();

        var memberLeaveAbsence = MemberLeaveAbsence.builder()
                .id(1)
                .member(target)
                .status(true)
                .expectedDateReturnSchool(LocalDate.of(2002,8,28))
                .build();

        //when
        var result = memberLeaveAbsenceRepository.save(memberLeaveAbsence);

        //then
        assertNotNull(result);
        assertEquals(1,result.getId());
        assertTrue(result.isStatus());
        assertEquals(LocalDate.of(2002,8,28),result.getExpectedDateReturnSchool());
        assertEquals(target,result.getMember());
    }

    @DisplayName("saveAndFlush test 1 - 정상적인 데이터가 주어지면, DB에 저장한다.")
    @Test
    void saveAndFlush(){
        //given
        entityManager.getEntityManager().createNativeQuery("DELETE FROM MEMBER_LEAVE_ABSENCE AS MLA WHERE MLA.MEMBER_LEAVE_ABSENCE_PK = :id").setParameter("id",1).executeUpdate();

        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(5L).name("ROLE_DEV").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();

        var memberLeaveAbsence = MemberLeaveAbsence.builder()
                .member(target)
                .status(false)
                .expectedDateReturnSchool(null)
                .build();

        //when

        var result = memberLeaveAbsenceRepository.saveAndFlush(memberLeaveAbsence);

        //then
        assertNotNull(result);
        assertEquals(7,result.getId());
        assertFalse(result.isStatus());
        assertNull(result.getExpectedDateReturnSchool());
        assertEquals(target,result.getMember());

    }
}