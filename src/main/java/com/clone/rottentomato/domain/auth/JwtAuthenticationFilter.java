package com.clone.rottentomato.domain.auth;

import com.clone.rottentomato.domain.auth.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtil.extractTokenFromHeader(request); // 헤더에서 토큰 추출

        // 헤더에 토큰이 없으면 쿠키에서 토큰 추출
        if (token == null) {
            token = jwtUtil.extractTokenFromCookie(request);
        }
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 이후 부분 추출
            token = token.replace("+", "");  // + 기호 제거
        }

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String email = jwtUtil.getUserIdFromToken(token);
                if (email != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    if (jwtUtil.validateToken(token)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (ExpiredJwtException e) {
                log.error("[토근 만료] : {}" , e.getMessage());
                jwtExceptionHandler(response , "토큰정보 만료" , HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                log.error("[토큰인증 장애] :{}" , e.getMessage());
                jwtExceptionHandler(response , "토큰인증 오류 : " + e.getMessage() , HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                return;
            }
        }else{
/*            jwtExceptionHandler(response, "토큰정보 없음" , HttpServletResponse.SC_UNAUTHORIZED);
            return;*/
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // 인증이 필요 없는 경로 설정
        return path.equals("/member/test") ||
                path.equals("/member/login") ||
                path.startsWith("/public"); // 추가적으로 public 경로 제외 등 필요 시
    }


    //예외발생 응답처리
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", msg);
            responseBody.put("result", false);
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        }catch (Exception ex){
            log.error("JWT Exception Handler Error : {}" , ex.getMessage());
        }
    }
}