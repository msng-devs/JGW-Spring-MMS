package com.jaramgroupware.mms.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RankApiControllerTest {

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
    void getRankById() {
        //given
        var url = "http://localhost:" + port + "/mms/api/v1/rank/1";

        var except = new RankResponseDto(1L,"게스트");

        var headers = new LinkedMultiValueMap<String, String>();

        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(gson.toJson(except), result.getBody());
    }

    @Test
    void getRankAll() {
        //given
        var url = "http://localhost:" + port + "/mms/api/v1/rank";

        var exceptList = List.of(
                new RankResponseDto(1L,"게스트"),
                new RankResponseDto(2L,"수습"),
                new RankResponseDto(3L,"정회원"),
                new RankResponseDto(4L,"준OB"),
                new RankResponseDto(5L,"OB"),
                new RankResponseDto(6L,"준회원")
        );

        var headers = new LinkedMultiValueMap<String, String>();

        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(gson.toJson(exceptList), result.getBody());
    }
}