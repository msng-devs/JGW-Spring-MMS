package com.jaramgroupware.mms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceAddRequestControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceUpdateRequestControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceResponseServiceDto;
import com.jaramgroupware.mms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static com.jaramgroupware.mms.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@SpringBootTest
public class MemberLeaveAbsenceApiControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private MemberLeaveAbsenceService memberLeaveAbsenceService;

    private final TestUtils testUtils = new TestUtils();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addMemberLeaveAbsence() throws Exception {
        //given
        MemberLeaveAbsenceAddRequestControllerDto memberLeaveAbsenceAddRequestControllerDto= MemberLeaveAbsenceAddRequestControllerDto.builder()
                .id(testUtils.getTestMemberLeaveAbsence().getId())
                .status(testUtils.getTestMemberLeaveAbsence().isStatus())
                .expectedDateReturnSchool(testUtils.getTestMemberLeaveAbsence().getExpectedDateReturnSchool())
                .build();

        doReturn(memberLeaveAbsenceAddRequestControllerDto.getId()).when(memberLeaveAbsenceService).add(any(MemberLeaveAbsenceAddRequestServiceDto.class));

        //when
        ResultActions result = mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/member-leave-absence")
                                .header("user_pk",testUtils.getTestUid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(memberLeaveAbsenceAddRequestControllerDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("member-leave-absence-add-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("대상 member의 UID (firebase uid)").attributes(field("constraints", "28자 firebase uid")),
                                fieldWithPath("status").description("대상 member의 휴학 여부").attributes(field("constraints", "true : 휴학 , false : 휴학 아님")),
                                fieldWithPath("expected_date_return_school").description("대상 member의 휴학 예정일").attributes(field("constraints", "yyyy-MM-dd format을 따라야함."))
                        ),
                        responseFields(
                                fieldWithPath("member_id").description(testUtils.getTestMember().getId())
                        )
                ));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.member_id").value(memberLeaveAbsenceAddRequestControllerDto.getId()));
        verify(memberLeaveAbsenceService).add(any(MemberLeaveAbsenceAddRequestServiceDto.class));
    }

    @Test
    void getMemberLeaveAbsence() throws Exception {
        //given
        String memberID = testUtils.getTestMemberLeaveAbsence().getId();

        MemberLeaveAbsenceResponseServiceDto targetMemberLeaveAbsenceDto = new MemberLeaveAbsenceResponseServiceDto(testUtils.getTestMemberLeaveAbsence());

        doReturn(targetMemberLeaveAbsenceDto).when(memberLeaveAbsenceService).findById(memberID);

        //when
        ResultActions result = mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/member-leave-absence/{memberID}",memberID)
                                .header("user_pk",testUtils.getTestUid())
                                .header("role_pk",4)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("member-leave-absence-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberID").description("대상 Member의 uid(firebase uid)")
                        ),
                        responseFields(
                                fieldWithPath("id").description("대상 member의 UID (firebase uid)").attributes(field("constraints", "28자 firebase uid")),
                                fieldWithPath("status").description("대상 member의 휴학 여부").attributes(field("constraints", "true : 휴학 , false : 휴학 아님")),
                                fieldWithPath("expected_date_return_school").description("대상 member의 휴학 예정일").attributes(field("constraints", "yyyy-MM-dd format을 따라야함.")))
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(targetMemberLeaveAbsenceDto.toControllerDto())));
        verify(memberLeaveAbsenceService).findById(memberID);
    }

    @Test
    void delMemberLeaveAbsence() throws Exception {
        //given
        String memberID = testUtils.getTestMemberLeaveAbsence().getId();

        doReturn(memberID).when(memberLeaveAbsenceService).delete(memberID);

        //when
        ResultActions result = mvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/member-leave-absence/{memberID}",memberID)
                                .header("user_pk",testUtils.getTestUid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("member-leave-absence-del-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberID").description("대상 Member의 uid(firebase uid)")
                        ),
                        responseFields(
                                fieldWithPath("member_id").description("대상 member의 UID (firebase uid)")
                        )
                ));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.member_id").value(memberID));
        verify(memberLeaveAbsenceService).delete(memberID);
    }

    @Test
    void updateMemberLeaveAbsence() throws Exception {
        //given
        String memberID = testUtils.getTestMemberLeaveAbsence().getId();

        MemberLeaveAbsenceUpdateRequestControllerDto testMemberLeaveAbsenceDto = MemberLeaveAbsenceUpdateRequestControllerDto.builder()
                .status(testUtils.getTestMemberLeaveAbsence().isStatus())
                .expectedDateReturnSchool(testUtils.getTestMemberLeaveAbsence2().getExpectedDateReturnSchool())
                .build();

        MemberLeaveAbsenceResponseServiceDto testMemberLeaveAbsenceResult = new MemberLeaveAbsenceResponseServiceDto(testUtils.getTestMemberLeaveAbsence());

        doReturn(testMemberLeaveAbsenceResult).when(memberLeaveAbsenceService).update(memberID,testMemberLeaveAbsenceDto.toServiceDto());

        //when
        ResultActions result = mvc.perform(
                        RestDocumentationRequestBuilders.put("/api/v1/member-leave-absence/{memberID}",memberID)
                                .header("user_pk",testUtils.getTestUid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testMemberLeaveAbsenceDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("member-leave-absence-update-single",
                        pathParameters(
                                parameterWithName("memberID").description("대상 Member의 uid(firebase uid)")
                        ),
                        requestFields(
                                fieldWithPath("status").description("대상 member의 휴학 여부").attributes(field("constraints", "true : 휴학 , false : 휴학 아님")),
                                fieldWithPath("expected_date_return_school").description("대상 member의 휴학 예정일").attributes(field("constraints", "yyyy-MM-dd format을 따라야함."))
                        ),
                        responseFields(
                                fieldWithPath("id").description("대상 member의 UID (firebase uid)").attributes(field("constraints", "28자 firebase uid")),
                                fieldWithPath("status").description("대상 member의 휴학 여부").attributes(field("constraints", "true : 휴학 , false : 휴학 아님")),
                                fieldWithPath("expected_date_return_school").description("대상 member의 휴학 예정일").attributes(field("constraints", "yyyy-MM-dd format을 따라야함.")))
                        ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testMemberLeaveAbsenceResult.toControllerDto())));
        verify(memberLeaveAbsenceService).update(memberID,testMemberLeaveAbsenceDto.toServiceDto());
    }

    //TODO getAll 구현하기

}