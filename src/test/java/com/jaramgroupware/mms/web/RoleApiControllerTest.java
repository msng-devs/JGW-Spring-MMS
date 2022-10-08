package com.jaramgroupware.mms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jaramgroupware.mms.TestUtils;
import com.jaramgroupware.mms.dto.role.controllerDto.RoleResponseControllerDto;
import com.jaramgroupware.mms.dto.role.serviceDto.RoleResponseServiceDto;
import com.jaramgroupware.mms.service.RoleService;
import com.jaramgroupware.mms.web.RoleApiController;
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
class RoleApiControllerTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private RoleService roleService;

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
    void getRoleById() throws Exception {
        //given
        Integer roleID = 1;

        RoleResponseServiceDto roleResponseServiceDto = RoleResponseServiceDto
                .builder()
                .id(roleID)
                .name("test")
                .build();

        doReturn(roleResponseServiceDto).when(roleService).findById(roleID);


        //when
        ResultActions result = mvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/role/{roleID}",roleID)
                        .header("user_uid",testUtils.getTestUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("role-get-single",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("roleID").description("대상 role의 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("대상 role의 id"),
                                fieldWithPath("name").description("대상 role의 name")
                        )
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roleResponseServiceDto.toControllerDto())));
        verify(roleService).findById(roleID);
    }

    @Test
    void getRoleAll() throws Exception {
        //given
        List<RoleResponseServiceDto> targetRoleList = new ArrayList<RoleResponseServiceDto>();

        RoleResponseServiceDto RoleResponseServiceDto1 = RoleResponseServiceDto
                .builder()
                .id(1)
                .name("test")
                .build();
        targetRoleList.add(RoleResponseServiceDto1);

        RoleResponseServiceDto RoleResponseServiceDto2 = RoleResponseServiceDto
                .builder()
                .id(2)
                .name("test")
                .build();
        targetRoleList.add(RoleResponseServiceDto2);

        doReturn(targetRoleList).when(roleService).findAll();

        //when
        ResultActions result = mvc.perform(
                get("/api/v1/role")
                        .header("user_uid",testUtils.getTestUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("role-get-multiple",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("대상 role의 id"),
                                fieldWithPath("[].name").description("대상 role의 name")
                        )
                ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(
                                targetRoleList.stream()
                                        .map(RoleResponseServiceDto::toControllerDto)
                                        .collect(Collectors.toList()))));
        verify(roleService).findAll();
    }
}