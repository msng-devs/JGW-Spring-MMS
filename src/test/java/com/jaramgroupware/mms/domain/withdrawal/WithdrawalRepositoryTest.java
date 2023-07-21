package com.jaramgroupware.mms.domain.withdrawal;

import com.jaramgroupware.mms.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WithdrawalRepositoryTest {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    //WITHDRAWAL_PK;MEMBER_MEMBER_PK;WITHDRAWAL_DEL_DATE;WITHDRAWAL_CREATE_DATE
    //1;HxPmZYJRgYQNWbvQitXQMXahQYvw;2023-07-19;2023-07-05

    //MEMBER_PK;MEMBER_NM;MEMBER_EMAIL;ROLE_ROLE_PK;MEMBER_STATUS
    //HxPmZYJRgYQNWbvQitXQMXahQYvw;나탈퇴;HxPmZYJRgYQNWbvQitXQMXahQYvw@test.com;3;0
    @Test
    void findByMember() {
        //given
        var targetMember = Member.builder().id("HxPmZYJRgYQNWbvQitXQMXahQYvw").build();

        //when
        var result = withdrawalRepository.findByMember(targetMember).orElse(null);

        //then
        assertNotNull(result);
        assertEquals("HxPmZYJRgYQNWbvQitXQMXahQYvw",result.getMember().getId());
        assertEquals("나탈퇴",result.getMember().getName());
        assertEquals(1,result.getId());
        assertEquals(LocalDate.of(2023,7,19),result.getWithdrawalDate());
        assertEquals(LocalDate.of(2023,7,5),result.getCreateDate());
    }

    @Test
    void save() {
        //given
        var targetMember = Member.builder().id("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ").build();
        var targetWithdrawal = Withdrawal.builder()
                .member(targetMember)
                .withdrawalDate(LocalDate.of(2023,7,19))
                .createDate(LocalDate.of(2023,7,5))
                .build();
        //when
        var result = withdrawalRepository.save(targetWithdrawal);

        //then
        assertEquals(2,result.getId());
        assertEquals("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ",result.getMember().getId());
        assertEquals(LocalDate.of(2023,7,19),result.getWithdrawalDate());
        assertEquals(LocalDate.of(2023,7,5),result.getCreateDate());
    }

    @Test
    void delete() {
        //given
        var target = Withdrawal.builder().id(1L).build();

        //when
        withdrawalRepository.delete(target);

        //then
    }
}