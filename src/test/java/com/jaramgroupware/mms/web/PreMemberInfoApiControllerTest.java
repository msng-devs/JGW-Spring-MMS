package com.jaramgroupware.mms.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.preMemberInfo.PreMemberInfoResponseDto;
import com.jaramgroupware.mms.dto.preMemberInfo.contollerDto.PreMemberInfoAddRequestControllerDto;
import com.jaramgroupware.mms.dto.preMemberInfo.contollerDto.PreMemberInfoUpdateRequestControllerDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.util.LinkedMultiValueMap;

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
class PreMemberInfoApiControllerTest {

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
    void getPreMemberInfoById() {
        //given
        var target = 2;

        var except = PreMemberInfoResponseDto.builder()
                .major(new MajorResponseDto(5L, "산업경영공학과"))
                .rank(new RankResponseDto(3L,"ROLE_USER0"))
                .role(new RoleResponseDto(5L, "OB"))
                .id(1L)
                .name("신규휴학")
                .studentId("2021000003")
                .year(37)
                .registerCode(null)
                .build();

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "0rGcxwzskwVaiG0p7Y2LXTMiwll5");
        headers.add("role_pk", "4");

        var url = "http://localhost:" + port + "/mms/api/v1/preMemberInfo/"+target;

        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        //then
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(gson.toJson(except), result.getBody());
    }

    @Test
    void getPreMemberInfoAll() {
        //given

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "0rGcxwzskwVaiG0p7Y2LXTMiwll5");
        headers.add("role_pk", "4");

        var except = PreMemberInfoResponseDto.builder()
                .major(new MajorResponseDto(5L, "산업경영공학과"))
                .rank(new RankResponseDto(3L,"ROLE_USER0"))
                .role(new RoleResponseDto(5L, "OB"))
                .id(1L)
                .name("신규휴학")
                .studentId("2021000003")
                .year(37)
                .registerCode(null)
                .build();
        //when
        var result = restTemplate.exchange("http://localhost:" + port + "/mms/api/v1/preMemberInfo", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
    }

    @Test
    void createPreMemberInfo() {
        //given
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "0rGcxwzskwVaiG0p7Y2LXTMiwll5");
        headers.add("role_pk", "4");
        headers.add("Content-Type", "application/json");

        var req = PreMemberInfoAddRequestControllerDto.builder()
                .majorId(1L)
                .rankId(1L)
                .roleId(1L)
                .name("test")
                .studentId("2021000010")
                .year(37)
                .expectedDateReturnSchool(null)
                .build();

        var reqJson = gson.toJson(req);

        var url = "http://localhost:" + port + "/mms/api/v1/preMemberInfo";

        var except = PreMemberInfoResponseDto.builder()
                .major(new MajorResponseDto(1L, "건설환경공학과"))
                .rank(new RankResponseDto(1L,"게스트"))
                .role(new RoleResponseDto(1L, "ROLE_GUEST"))
                .id(3L)
                .name("test")
                .studentId("2021000010")
                .year(37)
                .registerCode(null)
                .build();
        //when
        var result = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(reqJson,headers),String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(gson.toJson(except), result.getBody());
    }

    @Test
    void updatePreMemberInfo() {
        //given
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "0rGcxwzskwVaiG0p7Y2LXTMiwll5");
        headers.add("role_pk", "4");
        headers.add("Content-Type", "application/json");

        var req = PreMemberInfoUpdateRequestControllerDto.builder()
                .majorId(1L)
                .rankId(1L)
                .roleId(1L)
                .name("test")
                .studentId("2021000010")
                .year(37)
                .expectedDateReturnSchool(null)
                .build();

        var reqJson = gson.toJson(req);

        var url = "http://localhost:" + port + "/mms/api/v1/preMemberInfo/1";

        var except = PreMemberInfoResponseDto.builder()
                .major(new MajorResponseDto(1L, "건설환경공학과"))
                .rank(new RankResponseDto(1L,"게스트"))
                .role(new RoleResponseDto(1L, "ROLE_GUEST"))
                .id(1L)
                .name("test")
                .studentId("2021000010")
                .year(37)
                .registerCode(null)
                .build();
        //when
        var result = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(reqJson,headers),String.class);
        //then
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(gson.toJson(except), result.getBody());
    }

    @Test
    void deletePreMemberInfo() {
        //given
        var target = 1;
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "0rGcxwzskwVaiG0p7Y2LXTMiwll5");
        headers.add("role_pk", "4");

        var url = "http://localhost:" + port + "/mms/api/v1/preMemberInfo/1";
        //when
        var result = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers),String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals("OK", result.getBody());
    }
}