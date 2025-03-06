package com.clone.rottentomato.domain.member.controller;

import com.clone.rottentomato.domain.auth.JwtAuthenticationFilter;
import com.clone.rottentomato.domain.auth.JwtUtil;
import com.clone.rottentomato.domain.member.component.dto.MemberRequestDto;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.service.EmailService;
import com.clone.rottentomato.domain.member.service.GoogleMemberService;
import com.clone.rottentomato.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;;
    private final MemberService memberService;
    private final GoogleMemberService googleMemberService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final EmailService emailService; // EmailService 추가



    //구글인증 url 호출 Api
    @GetMapping("/login-url")
    public ResponseEntity<String> getGoogleLoginUrl() {
        String googleLoginUrl = googleMemberService.getGoogleAuthorizationUrl();
        return ResponseEntity.ok(googleLoginUrl);
    }

    //사용자 존제유무 확인
    @PostMapping("/check-member")
    public ResponseEntity<Boolean> checkMember(@RequestBody MemberRequestDto requestDto) {
    String email = requestDto.getEmail();
        return memberService.isExistMember(email) ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberRequestDto requestDto) {
        /*log.info("login :: --------------- Request information");
        log.info("login :: email: {} ",  requestDto .getEmail());
        log.info("login :: code: {} ",  requestDto .getAuthCode());*/

        // 1. 이메일 존재 확인 및 보안 코드 생성
       if(!memberService.isExistMember(requestDto.getEmail())){
            memberService.registerMember(requestDto.getEmail(), "LOCAL");
           log.info("login :: --------------- RegisterUser finished : TEST USER DATA INSERT");
        }
        Member member = memberService.findMemberByEmail(requestDto.getEmail());
        memberService.updateAuthCode(member);

        log.info("login :: --------------- Update authCode finished");
        log.info("updateAuthCode : {}" , member.getAuthCode());


        // 2. 이메일 전송
        String loginUrl = String.format("http://localhost:8080/member/login-code?code=%s&email=%s", member.getAuthCode(), requestDto.getEmail());
        String emailContent = String.format("로그인 링크: %s", loginUrl);
        /*log.info("login :: loginUrl: " + loginUrl + "");*/

        emailService.sendEmail(requestDto.getEmail(), "로그인 코드", emailContent);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login-code")
    public void loginWithCode(@RequestParam String code, @RequestParam String email, HttpServletResponse response) throws IOException {
        /*log.info("loginWithCode : code:  {}" ,code );
        log.info("loginWithCode : email:  {}" , email );*/

        Member member = memberService.findMemberByEmail(email);
        if(member == null) {
            log.info("loginWithCode : member is null");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/error");
            return;
        }
        String authCode = member.getAuthCode();
        log.info("loginWithCode : authCode:  {}" , authCode );

        if(authCode.equalsIgnoreCase(code)) {
            String jwt = jwtUtil.createToken(member.getMemberEmail());
            log.info("loginWithCode : jwt:  {}" , jwt );

            // JWT를 URL 인코딩하여 쿠키에 저장
            String encodedJwtToken = URLEncoder.encode(jwt, StandardCharsets.UTF_8);
            log.info("encodedJwtToken: " + encodedJwtToken);

            Cookie cookie = new Cookie("Authorization", encodedJwtToken);
            cookie.setPath("/"); // 쿠키 경로 설정
            cookie.setHttpOnly(true); // HttpOnly 설정
            cookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (1시간)

            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            response.sendRedirect("/testpage.html"); // 예시: 메인 페이지로 리다이렉트
        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/error");
        }

    }

    //현재 접속중인 유저 정보 호출
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
            }

            // UserDetails에서 사용자 정보 가져오기
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Member member = memberService.findMemberByEmail(email);

            if (member != null) {
                return ResponseEntity.ok(member);
            } else {
                return ResponseEntity.badRequest().body("해당 이메일의 회원 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("getUserInfo Error : {}", e.getMessage());
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }






    //로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            if(SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken oauthtoken) {
                authorizedClientService.removeAuthorizedClient(oauthtoken.getAuthorizedClientRegistrationId() , oauthtoken.getName());
            }
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            // ✅ JSESSIONID 쿠키 삭제
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            Cookie jwtCookie = new Cookie("Authorization", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(jwtCookie);

            return ResponseEntity.ok("LOGOUT_SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("LOGOUT_FAILED");
        }
    }
}
