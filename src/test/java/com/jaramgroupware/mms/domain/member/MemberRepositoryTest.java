package com.jaramgroupware.mms.domain.member;

import com.jaramgroupware.mms.domain.role.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("findMemberById test 1 - 올바른 유저의 id가 주어지면, 해당 유저의 정보를 반환한다.")
    @Test
    void findMemberById() {
        //given
        var targetId = "PFttxmS1wvspzHLkoHXaz4MOLUtE"; // 김임원,PFttxmS1wvspzHLkoHXaz4MOLUtE@test.com,4,1
        //when
        var result = memberRepository.findMemberById(targetId).orElse(null);

        //then
        assertNotNull(result);
        assertEquals(targetId,result.getId());
        assertEquals("김임원",result.getName());
        assertEquals("PFttxmS1wvspzHLkoHXaz4MOLUtE@test.com",result.getEmail());
        assertEquals(4,result.getRole().getId());
        assertTrue(result.isStatus());

    }

    @DisplayName("findMemberById test 2 - 올바르지 유저의 id가 주어지면, null을 리턴한다.")
    @Test
    void findMemberById2() {
        //given
        var targetId = "PFttxmS1wvspzHLkoHXaz4MOLNOT";
        //when
        var result = memberRepository.findMemberById(targetId).orElse(null);

        //then
        assertNull(result);

    }

    @DisplayName("existsByEmail test 1 - 존재하는 이메일이 주어지면, true를 리턴한다.")
    @Test
    void existsByEmail() {
        //given
        var existEmail = "HxPmZYJRgYQNWbvQitXQMXahQYvw@test.com";

        //when
        var result = memberRepository.existsByEmail(existEmail);

        //then
        assertTrue(result);
    }

    @DisplayName("existsByEmail test 2 - 존재하지 않는 이메일이 주어지면, false를 리턴한다.")
    @Test
    void existsByEmail2() {
        //given
        var notExistEmail = "thisIsNotExist@test.com";

        //when
        var result = memberRepository.existsByEmail(notExistEmail);

        //then
        assertFalse(result);
    }

    @DisplayName("save test 1 - 정상적인 정보를 입력하면, 새로 저장된 유저를 리턴한다.")
    @Test
    void save() {
        //given
        var testMember  = Member.builder()
                .id("10SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .name("testName")
                .email("test@test.com")
                .role(Role.builder().id(1L).name("ROLE_GUEST").build())
                .status(true)
                .build();
        //when
        var result = memberRepository.save(testMember);

        //then
        assertEquals(testMember,result);
    }

    @DisplayName("save test 2 - 이미 저장된 유저의 정보를 받으면, 해당 유저를 업데이트하고, 그결과를 리턴한다.")
    @Test
    void save2() {
        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(1L).name("ROLE_GUEST").build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();
        //when
        var result = memberRepository.save(target);

        //then
        assertEquals(target,result);
    }

    @DisplayName("delete test - 존재하는 멤버의 정보를 받으면, 해당 멤버를 제거한다.")
    @Test
    void delete(){
        //given
        var target = Member.builder()
                .id("ahOZkVnteYjzrI81hYqysTBGwHWU")
                .status(true)
                .name("김개발")
                .role(Role.builder().id(5L).build())
                .email("ahOZkVnteYjzrI81hYqysTBGwHWU@test.com")
                .build();
        //when
        memberRepository.delete(target);

        //then


    }
}