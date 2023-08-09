package com.jaramgroupware.mms.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.member.MemberRegisteredResponseDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.MemberLeaveAbsenceResponseDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceUpdateRequestControllerDto;
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
import static org.mockito.Mockito.doReturn;

@SqlGroup({
        @Sql(scripts = "classpath:DDL.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:DML.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeaveAbsenceApiControllerTest {

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
    void findLeaveAbsenceByMemberId() {
        //given
        var target = "DA8EPU9iPPhlGQPU8zwLfobQg1HB";
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "DA8EPU9iPPhlGQPU8zwLfobQg1HB");
        headers.add("role_pk", "2");


        var url = "http://localhost:" + port + "/mms/api/v1/member/"+ target +"/leave-absence";

        var except = MemberLeaveAbsenceResponseDto
                .builder()
                .id(2)
                .uid(target)
                .expectedDateReturnSchool(LocalDate.of(2024, 7, 5))
                .status(true)
                .build();

        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(gson.toJson(except), result.getBody());
    }

    @Test
    void findLeaveAbsenceByMemberId2() {
        //given
        var target = "PFttxmS1wvspzHLkoHXaz4MOLUtE";
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "DA8EPU9iPPhlGQPU8zwLfobQg1HB");
        headers.add("role_pk", "4");


        var url = "http://localhost:" + port + "/mms/api/v1/member/"+ target +"/leave-absence";

        var except = MemberLeaveAbsenceResponseDto
                .builder()
                .id(3)
                .uid(target)
                .expectedDateReturnSchool(null)
                .status(false)
                .build();

        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(gson.toJson(except), result.getBody());
    }
    @Test
    void updateLeaveAbsenceByMemberId() {
        //given
        var req = MemberLeaveAbsenceUpdateRequestControllerDto.builder()
                .isLeaveAbsence(true)
                .expectedReturnDate(LocalDate.of(2024, 7, 5))
                .build();

        var target = "ahOZkVnteYjzrI81hYqysTBGwHWU";

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "DA8EPU9iPPhlGQPU8zwLfobQg1HB");
        headers.add("role_pk", "4");
        headers.add("Content-Type", "application/json");

        var url = "http://localhost:" + port + "/mms/api/v1/member/"+target+"/leave-absence";
        var except = MemberLeaveAbsenceResponseDto
                .builder()
                .id(1)
                .uid(target)
                .expectedDateReturnSchool(LocalDate.of(2024, 7, 5))
                .status(true)
                .build();
        //when
        var result = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(gson.toJson(req), headers), String.class);
        //then
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(gson.toJson(except), result.getBody());
    }
}