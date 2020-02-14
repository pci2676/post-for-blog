package com.tistory.javabom.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tistory.javabom.member.domain.dto.MemberResDTO;
import com.tistory.javabom.member.domain.dto.MemberUpdateDTO;
import com.tistory.javabom.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.tistory.javabom.util.ApiDocumentUtils.getDocumentRequest;
import static com.tistory.javabom.util.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @DisplayName("멤버 정보 변경 API 문서화")
    @Test
    void updateMember() throws Exception {
        //given
        MemberResDTO resDTO = MemberResDTO.builder()
                .name("박찬인")
                .email("pci2676")
                .gitHub("github.com/pci2676")
                .build();

        given(memberService.updateMember(eq(1L), any(MemberUpdateDTO.class)))
                .willReturn(resDTO);

        //when
        MemberUpdateDTO memberUpdateDTO = MemberUpdateDTO.builder()
                .name("박찬인")
                .email("pci2676@gmail.com")
                .gitHub("github.com/pci2676")
                .build();

        ResultActions resultActions = this.mockMvc.perform(
                put("http://localhost:8080/api/v1/member/{id}", 1L)
                        .content(objectMapper.writeValueAsString(memberUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("Member-API",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("멤버 고유 식별자")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("바꿀 사용자 이름").optional(),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("바꿀 사용자 이메일"),
                                fieldWithPath("gitHub").type(JsonFieldType.STRING).description("바꿀 사용자 깃헙주소")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("바뀐 이름"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("바뀐 이메일"),
                                fieldWithPath("data.gitHub").type(JsonFieldType.STRING).description("바뀐 깃허브 주소")
                        )
                ));
    }
}