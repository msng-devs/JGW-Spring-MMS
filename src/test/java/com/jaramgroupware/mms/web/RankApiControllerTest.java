package com.jaramgroupware.mms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.dto.rank.controllerDto.RankResponseControllerDto;
import com.jaramgroupware.mms.dto.rank.serviceDto.RankResponseServiceDto;
import com.jaramgroupware.mms.service.RankService;
import com.jaramgroupware.mms.web.RankApiController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class RankApiControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private RankService rankService;

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
    void getRankById() throws Exception {
        //given
        Integer rankID = 1;

        RankResponseServiceDto majorResponseServiceDto = RankResponseServiceDto
                .builder()
                .id(rankID)
                .name("test")
                .build();

        doReturn(majorResponseServiceDto).when(rankService).findById(rankID);


        //when
        ResultActions result = mvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/rank/{rankID}",rankID)
                        .header("user_uid",testUtils.getTestUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("rank-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("rankID").description("대상 rank의 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("대상 rank의 id"),
                                fieldWithPath("name").description("대상 rank의 name")
                        )
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(majorResponseServiceDto.toControllerDto())));
        verify(rankService).findById(rankID);

    }

    @Test
    void getRankAll() throws Exception {
        //given
        List<RankResponseServiceDto> targetRankList = new ArrayList<RankResponseServiceDto>();

        RankResponseServiceDto RankResponseServiceDto1 = RankResponseServiceDto
                .builder()
                .id(1)
                .name("test")
                .build();
        targetRankList.add(RankResponseServiceDto1);

        RankResponseServiceDto RankResponseServiceDto2 = RankResponseServiceDto
                .builder()
                .id(2)
                .name("test")
                .build();
        targetRankList.add(RankResponseServiceDto2);

        doReturn(targetRankList).when(rankService).findAll();

        //when
        ResultActions result = mvc.perform(
                get("/api/v1/rank")
                        .header("user_uid",testUtils.getTestUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("rank-get-multiple",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("대상 major의 id"),
                                fieldWithPath("[].name").description("대상 major의 name")
                        )
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(targetRankList.stream()
                                .map(RankResponseServiceDto::toControllerDto).collect(Collectors.toList()))));
        verify(rankService).findAll();
    }
}