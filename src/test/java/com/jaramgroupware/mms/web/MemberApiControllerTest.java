package com.jaramgroupware.mms.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaramgroupware.mms.dto.major.MajorResponseDto;
import com.jaramgroupware.mms.dto.member.MemberRegisteredResponseDto;
import com.jaramgroupware.mms.dto.member.MemberResponseDto;
import com.jaramgroupware.mms.dto.member.MemberStat;
import com.jaramgroupware.mms.dto.member.StatusResponseDto;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberEditRequestControllerDto;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberRegisterRequestControllerDto;
import com.jaramgroupware.mms.dto.member.controllerDto.MemberUpdateRequestControllerDto;
import com.jaramgroupware.mms.dto.memberView.MemberViewDatailResponseDto;
import com.jaramgroupware.mms.dto.memberView.MemberViewResponseDto;
import com.jaramgroupware.mms.dto.rank.RankResponseDto;
import com.jaramgroupware.mms.dto.role.RoleResponseDto;
import com.jaramgroupware.mms.dto.withdrawal.WithdrawalResponseDto;
import com.jaramgroupware.mms.utils.time.TimeUtility;
import com.jaramgroupware.mms.utlis.json.LocalDateSerializer;
import com.jaramgroupware.mms.utlis.json.LocalDateTimeSerializer;
import com.jaramgroupware.mms.utlis.json.SnakeCaseFieldNamingStrategy;
import org.junit.jupiter.api.DisplayName;
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
class MemberApiControllerTest {

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
    void registerMember() {
        //given
        var requestDto = MemberRegisterRequestControllerDto.builder()
                .dateOfBirth(LocalDate.of(2021,7,5))
                .phoneNumber("010-0101-1111")
                .build();

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", "0rGcxwzskwVaiG0p7Y2LXTMiwll5");
        headers.add("user_email", "0rGcxwzskwVaiG0p7Y2LXTMiwll5@test.com");
        headers.add("Content-Type", "application/json");
        var code = "b691bb08-6d01-4aef-bf9e-54bb4a283496";


        //insert into JGW-TEST.PRE_MEMBER_INFO (PRE_MEMBER_INFO_PK, PRE_MEMBER_INFO_STUDENT_ID, PRE_MEMBER_INFO_YEAR, RANK_RANK_PK, MAJOR_MAJOR_PK, PRE_MEMBER_INFO_NM, ROLE_ROLE_PK, PRE_MEMBER_INFO_EXPECTED_DATE_RETURN_SCHOOL)
        //values  (2, '2021000004', 36, 4, 4, '일반', 2, null);
        var expected = MemberRegisteredResponseDto.builder()
                .uid("0rGcxwzskwVaiG0p7Y2LXTMiwll5")
                .name("일반")
                .email("0rGcxwzskwVaiG0p7Y2LXTMiwll5@test.com")
                .rank(RankResponseDto.builder()
                        .id(4L)
                        .name("준OB")
                        .build()
                )
                .major(MajorResponseDto.builder()
                        .id(4L)
                        .name("건축학부")
                        .build()
                )
                .studentId("2021000004")
                .year(36)
                .role(RoleResponseDto.builder()
                        .id(2L)
                        .name("ROLE_USER0")
                        .build()
                )
                .dateOfBirth(LocalDate.of(2021,7,5))
                .cellPhoneNumber("010-0101-1111")
                .leaveAbsence(null)
                .build();
        var exceptResultJson = gson.toJson(expected);
        doReturn(LocalDate.of(2021,7,5)).when(timeUtility).nowDate();

        var url = "http://localhost:" + port + "/mms/api/v1/member/register/" + code;
        //when
        var result = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(gson.toJson(requestDto), headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(exceptResultJson,result.getBody());
    }

    //insert into JGW-TEST.MEMBER_DATA_VIEW (MEMBER_PK, NAME, EMAIL, ROLE, ROLE_NAME, STATUS, CELL_PHONE_NUMBER, STUDENT_ID, YEAR, RANK, RANK_NAME, MAJOR, MAJOR_NAME, DATEOFBIRTH, IS_LEAVE_ABSENCE)
    //values  ('S0SfRrNYO9ggh2mdDO6qOfZB9iyJ', '나수습', 'S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com', 2, 'ROLE_USER0', 1, '010-2222-2222', '2021000006', 38, 2, '수습', 3, '건축공학전공', '2023-07-05', 0);
    @DisplayName("getMemberById test 1 - 기본 정보 조회를 하면, 기본 정보만을 준다.")
    @Test
    void getMemberById() {
        //given
        var target = "S0SfRrNYO9ggh2mdDO6qOfZB9iyJ";
        var exceptResult = MemberViewResponseDto.builder()
                .uid("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .rank(new RankResponseDto(2L,"수습"))
                .name("나수습")
                .email("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com")
                .major(new MajorResponseDto(3L,"건축공학전공"))
                .studentId("21")
                .year(38)
                .build();


        var exceptResultJson = gson.toJson(exceptResult);

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);
        headers.add("role_pk", "2");

        var url = "http://localhost:" + port + "/mms/api/v1/member/" + target;

        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(exceptResultJson,result.getBody());
    }

    @DisplayName("getMemberById test 2 - 상세 정보 조회를 하면, 상세한 정보를 준다.")
    @Test
    void getMemberById2() {
        //given
        var target = "S0SfRrNYO9ggh2mdDO6qOfZB9iyJ";
        var exceptResult = MemberViewDatailResponseDto.builder()
                .uid("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .rank(new RankResponseDto(2L,"수습"))
                .name("나수습")
                .email("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com")
                .major(new MajorResponseDto(3L,"건축공학전공"))
                .studentId("2021000006")
                .status(true)
                .dateOfBirth(LocalDate.of(2023,7,5))
                .isLeaveAbsence(false)
                .cellPhoneNumber("010-2222-2222")
                .role(new RoleResponseDto(2L,"ROLE_USER0"))
                .year(38)
                .build();

        var exceptResultJson = gson.toJson(exceptResult);

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);
        headers.add("role_pk", "2");

        var url = "http://localhost:" + port + "/mms/api/v1/member/" + target+"?isDetail=true";

        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(exceptResultJson,result.getBody());
    }
    //TODO 유닛테스트 page 관련 오류 해결하기
    @Test
    void getMemberAll(){
        //given
        var target = "S0SfRrNYO9ggh2mdDO6qOfZB9iyJ";
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);
        headers.add("role_pk", "4");

        var url = "http://localhost:" + port + "/mms/api/v1/member?major=3";

        var exceptResult = MemberViewDatailResponseDto.builder()
                .uid("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .rank(new RankResponseDto(2L,"수습"))
                .name("나수습")
                .email("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com")
                .major(new MajorResponseDto(3L,"건축공학전공"))
                .studentId("2021000006")
                .status(true)
                .dateOfBirth(LocalDate.of(2023,7,5))
                .isLeaveAbsence(false)
                .cellPhoneNumber("010-2222-2222")
                .role(new RoleResponseDto(2L,"ROLE_USER0"))
                .year(38)
                .build();

        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers),String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
//        assertEquals(1,result.getBody().getTotalElements());
//        assertEquals(exceptResult,result.getBody().getContent().get(0));
    }
    @Test
    void deleteMember() {
        //given
        var target = "S0SfRrNYO9ggh2mdDO6qOfZB9iyJ";
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);
        headers.add("role_pk", "4");

        var url = "http://localhost:" + port + "/mms/api/v1/member/"+target;
        //when
        var result = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<Object>(headers),String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals("OK",result.getBody());
    }


    @Test
    void updateMember() {
        //given
        var target = "S0SfRrNYO9ggh2mdDO6qOfZB9iyJ";
        //insert into JGW-TEST.MEMBER_DATA_VIEW (MEMBER_PK, NAME, EMAIL, ROLE, ROLE_NAME, STATUS, CELL_PHONE_NUMBER, STUDENT_ID, YEAR, RANK, RANK_NAME, MAJOR, MAJOR_NAME, DATEOFBIRTH, IS_LEAVE_ABSENCE)
        //values  ('S0SfRrNYO9ggh2mdDO6qOfZB9iyJ', '나수습', 'S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com', 2, 'ROLE_USER0', 1, '010-2222-2222', '2021000006', 38, 2, '수습', 3, '건축공학전공', '2023-07-05', 0);
        var request = MemberUpdateRequestControllerDto.builder()
                .majorId(3L)
                .rankId(2L)
                .year(38)
                .phoneNumber("010-2222-2222")
                .studentId("2021000006")
                .dateOfBirth(LocalDate.of(2023,7,5))
                .email("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com")
                .roleId(2L)
                .name("나수습")
                .build();

        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);
        headers.add("role_pk", "4");
        headers.add("Content-Type", "application/json");

        var url = "http://localhost:" + port + "/mms/api/v1/member/"+target;
        var reqJson = gson.toJson(request);

        var except = MemberResponseDto.builder()
                .year(38)
                .cellPhoneNumber("010-2222-2222")
                .studentId("2021000006")
                .dateOfBirth(LocalDate.of(2023,7,5))
                .email("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com")
                .name("나수습")
                .rank(new RankResponseDto(2L,"수습"))
                .role(new RoleResponseDto(2L,"ROLE_USER0"))
                .major(new MajorResponseDto(3L,"건축공학전공"))
                .uid("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .build();

        var exceptJson = gson.toJson(except);

        //when
        var result = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(reqJson,headers),String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(exceptJson,result.getBody());
    }

    @Test
    void withdrawalMember() {
        //given
        var target = "S0SfRrNYO9ggh2mdDO6qOfZB9iyJ";
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);
        headers.add("role_pk", "4");

        var url = "http://localhost:" + port + "/mms/api/v1/member/withdrawal";
        doReturn(LocalDate.of(2021,7,5)).when(timeUtility).nowDate();
        var except = WithdrawalResponseDto.builder().withdrawalDate(LocalDate.of(2021,7,12)).build();

        //when
        var result = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(gson.toJson(except),result.getBody());
    }

    @Test
    void cancelWithdrawalMember() {
        //given
        var target = "HxPmZYJRgYQNWbvQitXQMXahQYvw";
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);
        headers.add("role_pk", "4");

        var url = "http://localhost:" + port + "/mms/api/v1/member/withdrawal";
        //when
        var result = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals("OK",result.getBody());
    }

    @Test
    void editMember() {
        //given
        var target = "S0SfRrNYO9ggh2mdDO6qOfZB9iyJ";
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);
        headers.add("role_pk", "4");
        headers.add("Content-Type", "application/json");
        var request = MemberEditRequestControllerDto.builder()
                .majorId(1L)
                .phoneNumber("010-2222-2223")
                .name("나수습")
                .build();

        var url = "http://localhost:" + port + "/mms/api/v1/member/edit";

        var except = MemberResponseDto.builder()
                .major(new MajorResponseDto(1L,"건설환경공학과"))
                .cellPhoneNumber("010-2222-2223")
                .year(38)
                .email("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ@test.com")
                .role(new RoleResponseDto(2L,"ROLE_USER0"))
                .studentId("2021000006")
                .uid("S0SfRrNYO9ggh2mdDO6qOfZB9iyJ")
                .rank(new RankResponseDto(2L,"수습"))
                .dateOfBirth(LocalDate.of(2023,7,5))
                .name("나수습")
                .build();

        //when
        var result = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(gson.toJson(request),headers), String.class);

        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(gson.toJson(except),result.getBody());
    }

    @Test
    void getStatus() {
        //given
        var target = "S0SfRrNYO9ggh2mdDO6qOfZB9iyJ";

        var url = "http://localhost:" + port + "/mms/api/v1/member/status";
        var headers = new LinkedMultiValueMap<String, String>();
        headers.add("user_pk", target);

        var except = StatusResponseDto.builder().status(MemberStat.ACTIVATED.getStatus()).build();
        //when
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        //then
        assertEquals(HttpStatusCode.valueOf(200),result.getStatusCode());
        assertEquals(gson.toJson(except),result.getBody());
    }
}