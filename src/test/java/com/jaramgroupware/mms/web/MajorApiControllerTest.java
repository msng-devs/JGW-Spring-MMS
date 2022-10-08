package com.jaramgroupware.mms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.domain.major.MajorSpecification;
import com.jaramgroupware.mms.domain.major.MajorSpecificationBuilder;
import com.jaramgroupware.mms.dto.major.controllerDto.MajorResponseControllerDto;
import com.jaramgroupware.mms.dto.major.serviceDto.MajorResponseServiceDto;
import com.jaramgroupware.mms.service.MajorService;
import com.jaramgroupware.mms.web.MajorApiController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jaramgroupware.mms.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
class MajorApiControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private MajorSpecificationBuilder majorSpecificationBuilder;

    @MockBean
    private MajorService majorService;

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
    void getMajorById() throws Exception {
        //given
        Integer majorID = 1;

        MajorResponseServiceDto majorResponseServiceDto = MajorResponseServiceDto
                .builder()
                .id(majorID)
                .name("test")
                .build();

        doReturn(majorResponseServiceDto).when(majorService).findById(majorID);


        //when
        ResultActions result = mvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/major/{majorID}",majorID)
                        .header("user_uid",testUtils.getTestUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("major-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("majorID").description("대상 major의 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("대상 major의 id"),
                                fieldWithPath("name").description("대상 major의 name")
                        )
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(majorResponseServiceDto.toControllerDto())));
        verify(majorService).findById(majorID);
    }

    @Test
    void getMajorAll() throws Exception {
        //given
        List<MajorResponseServiceDto> targetMajorList = new ArrayList<MajorResponseServiceDto>();

        MajorResponseServiceDto majorResponseServiceDto = MajorResponseServiceDto
                .builder()
                .id(1)
                .name("test")
                .build();
        targetMajorList.add(majorResponseServiceDto);


        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        queryParam.add("name",majorResponseServiceDto.getName());

        Pageable pageable = PageRequest.of(0,1000, Sort.by(Sort.Direction.DESC,"id"));
        MajorSpecification spec = Mockito.mock(MajorSpecification.class);
        doReturn(spec).when(majorSpecificationBuilder).toSpec(queryParam);
        doReturn(targetMajorList).when(majorService).findAll(any(),any());

        //when
        ResultActions result = mvc.perform(
                get("/api/v1/major")
                        .header("user_uid",testUtils.getTestUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParams(queryParam))
                .andDo(print())
                .andDo(document("major-get-multiple",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("대상 major의 id"),
                                fieldWithPath("[].name").description("대상 major의 name")
                        )
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        targetMajorList.stream()
                                .map(MajorResponseServiceDto::toControllerDto)
                                .collect(Collectors.toList()))));

        verify(majorSpecificationBuilder).toSpec(queryParam);
        verify(majorService).findAll(any(),any());
    }
}