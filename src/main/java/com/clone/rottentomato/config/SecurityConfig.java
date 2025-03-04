package com.clone.rottentomato.config;


import com.clone.rottentomato.domain.auth.JwtAuthenticationFilter;
import com.clone.rottentomato.domain.auth.JwtUtil;
import com.clone.rottentomato.domain.auth.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil; // final 추가


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정

                //OAuth2.0  인증
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2LoginSuccessHandler(jwtUtil))
                        .failureHandler((request, response, exception) -> {
                            log.error("OAuth Login Fail : ", exception);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"로그인 실패\"}");
                        })
                )
                //JWT 인증
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        //미인증 경로 ( jwt인증이 필요없는 경우 이곳에 추가해야 합니다 )
                        .requestMatchers(
                                "/", //일반페이지
                                "/error",  //에러페이지
                                "/h2-console/**", //h2-Console 이하 경로
                                "/testpage.html", //테스트용 페이지
                                "/member/login", //일반 로그인 컨트롤러
                                "/member/check-member", //등록유무 확인 컨트롤러
                                "/member/login-code", //일반유저 보안코드 인증접속
                                "/login.html",
                                "/likes/{movie_id}/isLiked").permitAll() //로그인성공 페이지
                        //인증경로
/*                        .requestMatchers("/member/user-info", "/member/**").authenticated()*/
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout
                        //Cookie 삭제
                        .logoutUrl("/member/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("LOGOUT_SUCCESS");
                            response.getWriter().flush();
                        })));

        return http.build();
    }


    @Bean // CORS 설정 유지
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("*")); // 모든 메서드 허용
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

