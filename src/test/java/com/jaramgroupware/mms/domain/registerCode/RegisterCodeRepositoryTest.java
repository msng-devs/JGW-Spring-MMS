package com.jaramgroupware.mms.domain.registerCode;

import com.jaramgroupware.mms.config.TestQueryDslConfig;
import com.jaramgroupware.mms.domain.preMemberInfo.PreMemberInfo;
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

import java.time.LocalDate;
import java.util.UUID;

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
class RegisterCodeRepositoryTest {

    @Autowired
    private RegisterCodeRepository registerCodeRepository;

    @DisplayName("save test - 올바른 정보가 주어지면, 해당 정보를 저장한다.")
    @Test
    void save(){
        //given
        var target = RegisterCode.builder()
                .code("94ac4331-834f-4eac-9219-009280e82419")
                .createBy("test")
                .preMemberInfo(PreMemberInfo.builder().id(1L).build())
                .expiredAt(LocalDate.of(2021,7,1))
                .build();
        //when
        var result = registerCodeRepository.save(target);

        //then
        assertEquals(target.getCode(),result.getCode());
        assertEquals(target.getCreateBy(),result.getCreateBy());
        assertEquals(target.getPreMemberInfo().getId(),result.getPreMemberInfo().getId());
        assertEquals(target.getExpiredAt(),result.getExpiredAt());
        assertEquals(target.getPreMemberInfo().getId(),result.getPreMemberInfo().getId());
    }

    @DisplayName("delete test - 올바른 정보가 주어지면, 해당 정보를 삭제한다.")
    @Test
    void delete(){
        //given
        var target = "b691bb08-6d01-4aef-bf9e-54bb4a283496";

        //when
        registerCodeRepository.delete(RegisterCode.builder().code(target).build());

        //then

    }

    @DisplayName("findByCode test - 올바른 정보가 주어지면, 해당 정보를 리턴한다.")
    @Test
    void findByCode() {
        //given
        var target = "b691bb08-6d01-4aef-bf9e-54bb4a283496";

        //when
        var result = registerCodeRepository.findByCode(target).orElse(null);

        //then
        assertNotNull(result);
        assertEquals(target,result.getCode());
        assertEquals("KPzdvnpBicaIldPU8NRWFZMog0MP",result.getCreateBy());
        assertEquals(2L,result.getPreMemberInfo().getId());
        assertEquals(LocalDate.of(2023,7,6),result.getExpiredAt());

    }

    @DisplayName("findByCode test2 - 존재하지 않는 코드를 조회하면, null을 리턴한다.")
    @Test
    void findByCode2() {
        //given
        var target = "94ac4331-834f-4eac-9219-009280e82419";

        //when
        var result = registerCodeRepository.findByCode(target).orElse(null);

        //then
        assertNull(result);

    }

    @DisplayName("findByPreMemberInfo test - 올바른 정보가 주어지면, 해당 정보를 리턴한다.")
    @Test
    void findByPreMemberInfo() {
        //given
        var target = PreMemberInfo.builder().id(2L).build();
        //when
        var result = registerCodeRepository.findByPreMemberInfo(target).orElse(null);

        //then
        assertNotNull(result);
        assertEquals("b691bb08-6d01-4aef-bf9e-54bb4a283496",result.getCode());
        assertEquals("KPzdvnpBicaIldPU8NRWFZMog0MP",result.getCreateBy());
        assertEquals(2L,result.getPreMemberInfo().getId());
        assertEquals(LocalDate.of(2023,7,6),result.getExpiredAt());
    }

    @DisplayName("findByPreMemberInfo test2 - 존재하지 않은 정보가 주어지면, null를 리턴한다.")
    @Test
    void findByPreMemberInfo2() {
        //given
        var target = PreMemberInfo.builder().id(1L).build();
        //when
        var result = registerCodeRepository.findByPreMemberInfo(target).orElse(null);

        //then
        assertNull(result);
    }
}