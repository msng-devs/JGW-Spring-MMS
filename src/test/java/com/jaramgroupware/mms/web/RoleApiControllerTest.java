package com.jaramgroupware.mms.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RoleApiControllerTest {

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

    @Test
    void getRoleById() {
        //given
        var target = 1;
        var except = new RoleResponseDto(1L, "ROLE_GUEST");

        var url = "http://localhost:" + port + "/mms/api/v1/role/" + target;

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "ahOZkVnteYjzrI81hYqysTBGwHWU");
        headers.add("role_pk", "2");

        //when
        var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

        //then
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(except,gson.fromJson(response.getBody(), RoleResponseDto.class));
    }

    @Test
    void getRoleAll() {
        //given
        var except = List.of(
                new RoleResponseDto(5L, "ROLE_DEV"),
                new RoleResponseDto(4L, "ROLE_ADMIN"),
                new RoleResponseDto(3L, "ROLE_USER1"),
                new RoleResponseDto(2L, "ROLE_USER0"),
                new RoleResponseDto(1L, "ROLE_GUEST")
        );
        var url = "http://localhost:" + port + "/mms/api/v1/role";

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "ahOZkVnteYjzrI81hYqysTBGwHWU");
        headers.add("role_pk", "2");
        //when
        var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

        //then
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(gson.toJson(except),response.getBody());
    }
}