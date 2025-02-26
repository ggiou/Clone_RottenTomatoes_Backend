package com.clone.rottentomato.config;

import com.clone.rottentomato.domain.member.service.GoogleMemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final GoogleMemberService googleMemberService;

    public SecurityConfig(GoogleMemberService googleMemberService) {
        this.googleMemberService = googleMemberService;
    }
//    이건호 CORS 로직 만들어야함

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/movie/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // H2 Console은 CSRF 제외
                        .ignoringRequestMatchers("/movie/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()) // H2 Console 프레임 허용
                )

                /*.oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(googleMemberService)
                        )
                )*/;
        return http.build();
    }
}
