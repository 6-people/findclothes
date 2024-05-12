package com.people.findclothes.controller;

import com.google.gson.Gson;
import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.exception.UserAlreadyExistsException;
import com.people.findclothes.handler.CustomExceptionHandler;
import com.people.findclothes.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("회원 가입 - 존재하지 않는 사용자 정보이기 때문에 성공적으로 저장되는 경우")
    void 회원가입_성공() throws Exception {
        // given
        RequestUserSaveDto requestUserSaveDto = createRequestUserSaveDto();

        doNothing().when(userService).save(any(RequestUserSaveDto.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestUserSaveDto)));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원 가입 - 가입하려는 아이디, 이메일, 닉네임이 이미 존재하기 때문에 예외를 던지는 경우")
    void 회원가입_중복() throws Exception {
        // given
        RequestUserSaveDto requestUserSaveDto = createRequestUserSaveDto();

        doThrow(new UserAlreadyExistsException()).when(userService).save(any(RequestUserSaveDto.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestUserSaveDto))
        );

        // then
        resultActions.andExpect(status().is(400));
    }

    @Test
    @DisplayName("아이디 중복 검사")
    void 아이디중복검사() throws Exception {
        // given
        String id = "id";
        String id_duplicated = "duplicatedId";

        doReturn(false).when(userService).isDuplicatedId(id);
        doReturn(true).when(userService).isDuplicatedId(id_duplicated);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/register/isDuplicatedId")
                        .param("id", id)
        );
        ResultActions resultActions_duplicated = mockMvc.perform(
                get("/register/isDuplicatedId")
                        .param("id", id_duplicated)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();

        resultActions_duplicated.andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
    }

    @Test
    @DisplayName("이메일 중복 검사")
    void 이메일중복검사() throws Exception {
        // given
        String email = "email@google.com";
        String email_duplicated = "duplicatedEmail@google.com";

        doReturn(false).when(userService).isDuplicatedEmail(email);
        doReturn(true).when(userService).isDuplicatedEmail(email_duplicated);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/register/isDuplicatedEmail")
                        .param("email", email)
        );
        ResultActions resultActions_duplicated = mockMvc.perform(
                get("/register/isDuplicatedEmail")
                        .param("email", email_duplicated)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();

        resultActions_duplicated.andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
    }

    @Test
    @DisplayName("닉네임 중복 검사")
    void 닉네임중복검사() throws Exception {
        // given
        String nickname = "nickname";
        String nickname_duplicated = "duplicatedNickname";

        doReturn(false).when(userService).isDuplicatedNickname(nickname);
        doReturn(true).when(userService).isDuplicatedNickname(nickname_duplicated);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/register/isDuplicatedNickname")
                        .param("nickname", nickname)
        );
        ResultActions resultActions_duplicated = mockMvc.perform(
                get("/register/isDuplicatedNickname")
                        .param("nickname", nickname_duplicated)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();

        resultActions_duplicated.andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
    }

    public RequestUserSaveDto createRequestUserSaveDto() {
        return RequestUserSaveDto.builder()
                .id("userId")
                .email("email@google.com")
                .nickname("nickname")
                .password("1234")
                .build();
    }
}