package com.jaramgroupware.mms.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MajorApiControllerTest {

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
    void getMajorById() {
        //given
        var url = "http://localhost:" + port + "/mms/api/v1/major/1";
        var except = new MajorResponseDto(1L,"건설환경공학과");

        //when
        var response = restTemplate.getForEntity(url, String.class);
        //then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), gson.toJson(except));

    }

    @Test
    void findAll(){
        //given
        var url = "http://localhost:" + port + "/mms/api/v1/major";
        //when
        var response = restTemplate.getForEntity(url, String.class);
        //then
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}