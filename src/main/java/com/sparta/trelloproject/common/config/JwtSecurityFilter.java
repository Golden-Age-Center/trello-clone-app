package com.sparta.trelloproject.common.config;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static com.sparta.trelloproject.common.constants.Const.USER_EMAIL;
import static com.sparta.trelloproject.common.constants.Const.USER_ROLE;

@Slf4j(topic = "JwtTokenFilter")
@RequiredArgsConstructor
@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final List<String> whiteList = List.of("^/api/v(?:[1-9])/auth/[a-zA-Z\\-]+$","/actuator/prometheus");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (Strings.isNotBlank(url) && checkUrlPattern(url)) {
            // 나머지 API 요청은 인증 처리 진행
            // 토큰 확인
            String tokenValue = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (Strings.isNotBlank(tokenValue)) { // 토큰이 존재하면 검증 시작
                // 토큰 검증
                String token = jwtUtil.substringToken(tokenValue);

                if (!jwtUtil.validateToken(token)) {
                    log.error("인증 실패");
                    response.setContentType("application/json");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증에 실패했습니다.");
                    return;
                } else {
                    log.info("토큰 검증 성공");
                    Claims claims = jwtUtil.getUserInfoFromToken(token);

                    Long userId = Long.parseLong(claims.getSubject());
                    String email = claims.get(USER_EMAIL, String.class);
                    UserRole userRole = UserRole.of(claims.get(USER_ROLE, String.class));

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        AuthUser authUser = new AuthUser(userId, email, userRole);

                        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }

                }
            } else {
                log.error("토큰이 없습니다.");
                response.setContentType("application/json");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "토큰이 없습니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public boolean checkUrlPattern(String url) {
        // 패턴과 하나도 일치하는게 없다면 jwt token 필요
        return whiteList.stream()
                .noneMatch(whiteUrl -> Pattern.matches(whiteUrl, url));
    }

}
