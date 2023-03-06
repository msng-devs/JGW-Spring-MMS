package com.jaramgroupware.mms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.member.Member;
import com.jaramgroupware.mms.domain.memberLeaveAbsence.MemberLeaveAbsence;
import com.jaramgroupware.mms.dto.member.serviceDto.MemberResponseServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceAddRequestControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceBulkDeleteRequestControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.controllerDto.MemberLeaveAbsenceUpdateRequestControllerDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceAddRequestServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceResponseServiceDto;
import com.jaramgroupware.mms.dto.memberLeaveAbsence.serviceDto.MemberLeaveAbsenceUpdateRequestServiceDto;
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


import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jaramgroupware.mms.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.*;
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

    @MockBean
    private MemberService memberService;

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
        Integer id = testUtils.getTestMemberLeaveAbsence().getId();
        MemberLeaveAbsenceAddRequestControllerDto memberLeaveAbsenceAddRequestControllerDto= MemberLeaveAbsenceAddRequestControllerDto.builder()
                .memberId(testUtils.getTestMemberLeaveAbsence().getMember().getId())
                .status(testUtils.getTestMemberLeaveAbsence().isStatus())
                .expectedDateReturnSchool(testUtils.getTestMemberLeaveAbsence().getExpectedDateReturnSchool())
                .build();

        doReturn(id).when(memberLeaveAbsenceService).add(any(MemberLeaveAbsenceAddRequestServiceDto.class));

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
                                fieldWithPath("member_id").description("대상 member의 UID (firebase uid)").attributes(field("constraints", "28자 firebase uid")),
                                fieldWithPath("status").description("대상 member의 휴학 여부").attributes(field("constraints", "true : 휴학 , false : 휴학 아님")),
                                fieldWithPath("expected_date_return_school").description("대상 member의 휴학 예정일").attributes(field("constraints", "yyyy-MM-dd format을 따라야함."))
                        ),
                        responseFields(
                                fieldWithPath("id").description("추가된 member leave absence의 id")
                        )
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        verify(memberLeaveAbsenceService).add(any(MemberLeaveAbsenceAddRequestServiceDto.class));
    }

    @Test
    void getMemberLeaveAbsence() throws Exception {
        //given
        String memberID = testUtils.getTestMemberLeaveAbsence().getMember().getId();

        MemberLeaveAbsenceResponseServiceDto memberLeaveAbsenceResponseServiceDto = new MemberLeaveAbsenceResponseServiceDto(testUtils.getTestMemberLeaveAbsence());
        MemberResponseServiceDto memberResponseServiceDto = new MemberResponseServiceDto(testUtils.getTestMember());

        doReturn(memberResponseServiceDto).when(memberService).findById(memberID);
        doReturn(memberLeaveAbsenceResponseServiceDto).when(memberLeaveAbsenceService).findByMember(any(Member.class));

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
                                fieldWithPath("id").description("대상 member leave absence의 id"),
                                fieldWithPath("member_id").description("대상 member의 UID (firebase uid)").attributes(field("constraints", "28자 firebase uid")),
                                fieldWithPath("status").description("대상 member의 휴학 여부").attributes(field("constraints", "true : 휴학 , false : 휴학 아님")),
                                fieldWithPath("expected_date_return_school").description("대상 member의 휴학 예정일").attributes(field("constraints", "yyyy-MM-dd format을 따라야함.")))
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberLeaveAbsenceResponseServiceDto.toControllerDto())));
        verify(memberService).findById(memberID);
        verify(memberLeaveAbsenceService).findByMember(any(Member.class));
    }

    @Test
    void getMemberLeaveAbsenceAll() throws Exception {
        //given
        List<MemberLeaveAbsenceResponseServiceDto> targetMemberLeaveAbsenceList = new ArrayList<MemberLeaveAbsenceResponseServiceDto>();

        MemberLeaveAbsenceResponseServiceDto targetMemberLeaveAbsenceDto1 = new MemberLeaveAbsenceResponseServiceDto(testUtils.getTestMemberLeaveAbsence());
        targetMemberLeaveAbsenceList.add(targetMemberLeaveAbsenceDto1);

        MemberLeaveAbsenceResponseServiceDto targetMemberLeaveAbsenceDto2 = new MemberLeaveAbsenceResponseServiceDto(testUtils.getTestMemberLeaveAbsence2());
        targetMemberLeaveAbsenceList.add(targetMemberLeaveAbsenceDto2);

        doReturn(targetMemberLeaveAbsenceList).when(memberLeaveAbsenceService).findAll(any(),any());

        //when
        ResultActions result = mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/member-leave-absence/")
                                .header("user_pk",testUtils.getTestUid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("member-leave-absence-get-multiple",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("대상 member leave absence의 id"),
                                fieldWithPath("[].member_id").description("대상 member의 UID (firebase uid)").attributes(field("constraints", "28자 firebase uid")),
                                fieldWithPath("[].status").description("대상 member의 휴학 여부").attributes(field("constraints", "true : 휴학 , false : 휴학 아님")),
                                fieldWithPath("[].expected_date_return_school").description("대상 member의 휴학 예정일").attributes(field("constraints", "yyyy-MM-dd format을 따라야함.")))
                        ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(targetMemberLeaveAbsenceList.stream()
                                .map(MemberLeaveAbsenceResponseServiceDto::toControllerDto)
                                .collect(Collectors.toList()))));
        verify(memberLeaveAbsenceService).findAll(any(),any());
    }

    @Test
    void delMemberLeaveAbsence() throws Exception {
        //given
        Member member = testUtils.getTestMemberLeaveAbsence().getMember();
        String memberID = member.getId();
        Integer id = testUtils.getTestMemberLeaveAbsence().getId();

        MemberResponseServiceDto memberResponseServiceDto = new MemberResponseServiceDto(member);

        doReturn(memberResponseServiceDto).when(memberService).findById(memberID);
        doReturn(id).when(memberLeaveAbsenceService).delete(any(Member.class));

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
                                fieldWithPath("id").description("삭제된 member leave absence의 id")
                        )
                ));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        verify(memberLeaveAbsenceService).delete(any(Member.class));
    }

    //TODO 서버 에러 나는 부분 고치기
//    @Test
//    void bulkDelete() throws  Exception{
//        //given
//        Set<Integer> ids = new HashSet<>();
//        ids.add(testUtils.getTestMemberLeaveAbsence().getId());
//        ids.add(testUtils.getTestMemberLeaveAbsence2().getId());
//
//        MemberLeaveAbsenceBulkDeleteRequestControllerDto dto = MemberLeaveAbsenceBulkDeleteRequestControllerDto.builder()
//                .MemberLeaveAbsenceIDs(ids)
//                .build();
//
//        //when
//        ResultActions result = mvc.perform(
//                        RestDocumentationRequestBuilders.delete("/api/v1/member-leave-absence")
//                                .header("user_pk",testUtils.getTestUid())
//                                .header("role_pk",4)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(dto)))
//                .andDo(print())
//                .andDo(document("member-leave-absence-del-bulk",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("member_leave_absence_ids").description("삭제할 MemberLeaveAbsence의 id")
//                        ),
//                        responseFields(
//                                fieldWithPath("message").description("삭제된 MemberLeaveAbsence의 갯수 ")
//                        )
//                ));
//
//        //then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("총 (2)개의 MemberLeaveAbsence를 성공적으로 삭제했습니다!"));
//        verify(memberLeaveAbsenceService).delete(anySet());
//    }

    @Test
    void updateMemberLeaveAbsence() throws Exception {
        //given
        Member member = testUtils.getTestMemberLeaveAbsence().getMember();
        String memberID = member.getId();

        MemberLeaveAbsenceUpdateRequestControllerDto memberLeaveAbsenceUpdateRequestControllerDto = MemberLeaveAbsenceUpdateRequestControllerDto.builder()
                .status(testUtils.getTestMemberLeaveAbsence().isStatus())
                .expectedDateReturnSchool(testUtils.getTestMemberLeaveAbsence().getExpectedDateReturnSchool())
                .build();

        MemberLeaveAbsenceResponseServiceDto memberLeaveAbsenceResponseServiceDto = new MemberLeaveAbsenceResponseServiceDto(testUtils.getTestMemberLeaveAbsence());
        MemberResponseServiceDto memberResponseServiceDto = new MemberResponseServiceDto(member);

        doReturn(memberResponseServiceDto).when(memberService).findById(memberID);
        doReturn(memberLeaveAbsenceResponseServiceDto).when(memberLeaveAbsenceService).update(any(Member.class),any(MemberLeaveAbsenceUpdateRequestServiceDto.class));

        //when
        ResultActions result = mvc.perform(
                        RestDocumentationRequestBuilders.put("/api/v1/member-leave-absence/{memberID}",memberID)
                                .header("user_pk",testUtils.getTestUid())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(memberLeaveAbsenceUpdateRequestControllerDto))
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
                                fieldWithPath("id").description("수정된 member leave absence의 id"),
                                fieldWithPath("member_id").description("대상 member의 UID (firebase uid)").attributes(field("constraints", "28자 firebase uid")),
                                fieldWithPath("status").description("대상 member의 휴학 여부").attributes(field("constraints", "true : 휴학 , false : 휴학 아님")),
                                fieldWithPath("expected_date_return_school").description("대상 member의 휴학 예정일").attributes(field("constraints", "yyyy-MM-dd format을 따라야함.")))
                        ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberLeaveAbsenceResponseServiceDto.toControllerDto())));
        verify(memberLeaveAbsenceService).update(any(Member.class),any(MemberLeaveAbsenceUpdateRequestServiceDto.class));
    }
}