package com.jaramgroupware.mms.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaramgroupware.mms.dto.registerCode.RegisterCodeResponseDto;
import com.jaramgroupware.mms.dto.registerCode.controllerDto.RegisterCodeAddRequestControllerDto;
import com.jaramgroupware.mms.utils.code.CodeGenerator;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import com.jaramgroupware.mms.utlis.json.LocalDateSerializer;
import com.jaramgroupware.mms.utlis.json.LocalDateTimeSerializer;
import com.jaramgroupware.mms.utlis.json.SnakeCaseFieldNamingStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RegisterCodeApiControllerTest {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingStrategy(new SnakeCaseFieldNamingStrategy())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .serializeNulls()
            .create();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private TimeUtility timeUtility;

    @MockBean
    private CodeGenerator codeGenerator;

    @Test
    void createRegisterCode() {
        //given
        var req = RegisterCodeAddRequestControllerDto.builder()
                .expireDay(7L)
                .build();

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "0rGcxwzskwVaiG0p7Y2LXTMiwll5");
        headers.add("role_pk", "4");
        headers.add("Content-Type", "application/json");

        var url = "http://localhost:" + port + "/mms/api/v1/preMemberInfo/1/code";

        doReturn(LocalDate.of(2021, 1, 1)).when(timeUtility).nowDate();
        doReturn("test").when(codeGenerator).generate();

        var expected = RegisterCodeResponseDto.builder()
                .code("test")
                .expiredDateTime(LocalDate.of(2021, 1, 8))
                .preMemberInfoId(1L)
                .build();

        //when
        var response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(gson.toJson(req),headers),String.class);
        //then
        assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
        assertEquals(gson.toJson(expected),response.getBody());
    }

    @Test
    void deleteRegisterCode() {
        //given
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "0rGcxwzskwVaiG0p7Y2LXTMiwll5");
        headers.add("role_pk", "4");


        var url = "http://localhost:" + port + "/mms/api/v1/preMemberInfo/2/code";
        //when
        var response = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers),String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
        assertEquals("OK",response.getBody());
    }
}