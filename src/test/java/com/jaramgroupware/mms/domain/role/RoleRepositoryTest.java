package com.jaramgroupware.mms.domain.role;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @DisplayName("findById test 1 - 존재하는 role의 id가 주어지면, 해당 정보를 리턴한다.")
    @Test
    void findRoleById() {
        //given
        var targetId = 1L;

        //when
        var result = roleRepository.findById(targetId).orElse(null);

        //then
        assertNotNull(result);
        assertEquals("ROLE_GUEST",result.getName());
        assertEquals(targetId,result.getId());
    }

    @DisplayName("findAllBy test - 존재하는 모든 Role의 정보를 리턴한다.")
    @Test
    void findAllByOrderByIdDesc() {
        //given

        //when
        var result = roleRepository.findAllByOrderByIdDesc();

        //then
        assertEquals(5,result.size());

        assertEquals(1,result.get(4).getId());
        assertEquals("ROLE_GUEST",result.get(4).getName());

        assertEquals(2,result.get(3).getId());
        assertEquals("ROLE_USER0",result.get(3).getName());

        assertEquals(3,result.get(2).getId());
        assertEquals("ROLE_USER1",result.get(2).getName());

        assertEquals(4,result.get(1).getId());
        assertEquals("ROLE_ADMIN",result.get(1).getName());

        assertEquals(5,result.get(0).getId());
        assertEquals("ROLE_DEV",result.get(0).getName());

    }
}