package com.clone.rottentomato.config;

import com.clone.rottentomato.domain.auth.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("[OAuth Authentication Success]");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String jwtToken = jwtUtil.createToken(email);

        // JWT를 URL 인코딩하여 쿠키에 저장
        String encodedJwtToken = URLEncoder.encode(jwtToken, StandardCharsets.UTF_8);
        log.info("encodedJwtToken: " + encodedJwtToken);

        Cookie cookie = new Cookie("Authorization", encodedJwtToken);
        cookie.setPath("/"); // 쿠키 경로 설정
        cookie.setHttpOnly(true); // HttpOnly 설정
        cookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (1시간)

        // 리다이렉션 대신 성공 응답 반환
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "로그인 성공");
        responseBody.put("result", true);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));

    }
}
