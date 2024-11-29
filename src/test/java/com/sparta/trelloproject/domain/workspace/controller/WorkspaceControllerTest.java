package com.sparta.trelloproject.domain.workspace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.trelloproject.common.config.JwtUtil;
import com.sparta.trelloproject.domain.user.enums.UserRole;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceInviteRequestDto;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceRequestDto;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceResponseDto;
import com.sparta.trelloproject.domain.workspace.service.WorkspaceService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WorkspaceControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private WorkspaceService workspaceService;

    @MockBean
    private RedisTemplate<String, Object> redisTemplate;

    @MockBean
    private RedissonClient redissonClient;  // RedissonClient를 Mocking

    @Test
    public void addWorkspace_동작완료() throws Exception {
        // given
        WorkspaceRequestDto requestDto = new WorkspaceRequestDto("워크스페이스명","워크스페이스설명");
        String postInfo = objectMapper.writeValueAsString(requestDto);

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).addWorkspace(any(), any());

        // then
        mvc.perform(post("/api/v1/workspaces")
                .header(HttpHeaders.AUTHORIZATION , token)
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("정상 처리되었습니다."))
                .andDo(print());
    }

    @Test
    public void addWorkspace_워크스페이스명_빈값_검증() throws Exception {
        // given
        WorkspaceRequestDto requestDto = new WorkspaceRequestDto(null ,"워크스페이스설명");
        String postInfo = objectMapper.writeValueAsString(requestDto);

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).addWorkspace(any(), any());

        // then
        mvc.perform(post("/api/v1/workspaces")
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("[워크스페이스명은 필수 입력 값입니다.]"))
                .andDo(print());
    }

    @Test
    public void inviteMemberToWorkspace_동작완료() throws Exception {
        // given
        WorkspaceInviteRequestDto requestDto = new WorkspaceInviteRequestDto("test@test.com",1L , "ROLE_WORKSPACE_ADMIN");
        String postInfo = objectMapper.writeValueAsString(requestDto);

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).inviteMemberToWorkspace(any(), any());

        // then
        mvc.perform(post("/api/v1/workspaces/invite")
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("정상 처리되었습니다."))
                .andDo(print());
    }

    @Test
    public void inviteMemberToWorkspace_이메일_검증() throws Exception {
        // given
        WorkspaceInviteRequestDto requestDto = new WorkspaceInviteRequestDto(null,1L , "ROLE_WORKSPACE_ADMIN");
        String postInfo = objectMapper.writeValueAsString(requestDto);

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).inviteMemberToWorkspace(any(), any());

        // then
        mvc.perform(post("/api/v1/workspaces/invite")
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("[이메일은 필수 입력 값입니다.]"))
                .andDo(print());
    }

    @Test
    public void inviteMemberToWorkspace_워크스페이스_ID_검증() throws Exception {
        // given
        WorkspaceInviteRequestDto requestDto = new WorkspaceInviteRequestDto("test@test.com",null , "ROLE_WORKSPACE_ADMIN");
        String postInfo = objectMapper.writeValueAsString(requestDto);

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).inviteMemberToWorkspace(any(), any());

        // then
        mvc.perform(post("/api/v1/workspaces/invite")
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("[워크스페이스 ID는 필수 입력 값입니다.]"))
                .andDo(print());
    }

    @Test
    public void inviteMemberToWorkspace_워크스페이스_역할_검증() throws Exception {
        // given
        WorkspaceInviteRequestDto requestDto = new WorkspaceInviteRequestDto("test@test.com",1L , null);
        String postInfo = objectMapper.writeValueAsString(requestDto);

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).inviteMemberToWorkspace(any(), any());

        // then
        mvc.perform(post("/api/v1/workspaces/invite")
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("[워크스페이스 역할은 필수 입력 값입니다.]"))
                .andDo(print());
    }

    @Test
    public void getWorkspaces_동작완료() throws Exception {
        // given
        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        List<WorkspaceResponseDto> workspaceResponseDtos = List.of(
                new WorkspaceResponseDto(1L , "워크스페이스명" , "워크스페이스설명")
        );
        given(workspaceService.getWorkspaces(any())).willReturn(workspaceResponseDtos);

        // then
        mvc.perform(get("/api/v1/workspaces")
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("정상 처리되었습니다."))
                .andDo(print());
    }

    @Test
    public void editWorkspace_동작완료() throws Exception {
        // given
        Long workspaceId = 1L;
        WorkspaceRequestDto requestDto = new WorkspaceRequestDto("워크스페이스명","워크스페이스설명");
        String postInfo = objectMapper.writeValueAsString(requestDto);

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).editWorkspace(any(), anyLong(), any());

        // then
        mvc.perform(patch("/api/v1/workspaces/{workspaceId}" , workspaceId)
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("정상 처리되었습니다."))
                .andDo(print());
    }

    @Test
    public void editWorkspace_워크스페이스명_검증() throws Exception {
        // given
        Long workspaceId = 1L;
        WorkspaceRequestDto requestDto = new WorkspaceRequestDto(null,"워크스페이스설명");
        String postInfo = objectMapper.writeValueAsString(requestDto);

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).editWorkspace(any(), anyLong(), any());

        // then
        mvc.perform(patch("/api/v1/workspaces/{workspaceId}" , workspaceId)
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("[워크스페이스명은 필수 입력 값입니다.]"))
                .andDo(print());
    }

    @Test
    public void removeWorkspace_동작완료() throws Exception {
        // given
        Long workspaceId = 1L;

        String token = jwtUtil.createToken(1L , "test@test.com" , UserRole.ROLE_ADMIN);

        // when
        doNothing().when(workspaceService).editWorkspace(any(), anyLong(), any());

        // then
        mvc.perform(delete("/api/v1/workspaces/{workspaceId}" , workspaceId)
                        .header(HttpHeaders.AUTHORIZATION , token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("정상 처리되었습니다."))
                .andDo(print());
    }
}