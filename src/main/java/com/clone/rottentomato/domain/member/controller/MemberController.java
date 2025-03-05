package com.clone.rottentomato.domain.member.controller;

import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.domain.auth.JwtUtil;
import com.clone.rottentomato.domain.member.component.dto.MemberRequestDto;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.service.EmailService;
import com.clone.rottentomato.domain.member.service.GoogleMemberService;
import com.clone.rottentomato.domain.member.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final JwtUtil jwtUtil;
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
    @PostMapping("/isExist")
    public CommonResponse checkMember(@RequestBody MemberRequestDto requestDto) {
        try {
            String email = requestDto.getEmail();
            if (email == null || email.trim().isEmpty()) {
                return CommonResponse.fail("이메일이 유효하지 않습니다.");
            }

            return memberService.isExistMember(email);

        }catch (Exception ex){
            log.error("회원조회 오류 : {}", ex.getMessage());
            return CommonResponse.error("회원조회 오류 발생 : " + ex.getMessage());
        }
    }

    //일반 로그인( 코드 생성 및 메일전달 )
    @PostMapping("/login")
    public CommonResponse login(@RequestBody MemberRequestDto requestDto) {
        try {
            // 1. 이메일 존재 확인 및 보안 코드 생성
            Boolean isExistMember = (Boolean) memberService.isExistMember(requestDto.getEmail()).getData();

            if (!isExistMember) {
                memberService.registerMember(requestDto.getEmail(), "LOCAL");
                log.info("login :: --------------- RegisterUser finished : TEST USER DATA INSERT");
            }
            Member member = memberService.findMemberByEmail(requestDto.getEmail());
            memberService.updateAuthCode(member);

            log.info("login :: --------------- Update authCode finished");
            log.info("updateAuthCode : {}", member.getAuthCode());

            // 2. 이메일 전송
            String loginUrl = String.format("http://localhost:8080/member/login-code?code=%s&email=%s", member.getAuthCode(), requestDto.getEmail());
            String emailContent = String.format("로그인 링크: %s", loginUrl);

            return emailService.sendEmail(requestDto.getEmail(), "로그인 코드", emailContent);
        } catch (Exception ex) {
            log.error("[일반]로그인 처리 중 오류 발생 : {}", ex.getMessage(), ex);
            return CommonResponse.error("로그인 처리 중 오류 발생 : " + ex.getMessage());
        }
    }

    //인증로그인
    @GetMapping("/login-code")
    public CommonResponse loginWithCode(@RequestParam String code, @RequestParam String email, HttpServletResponse response) {
        try {
            // Service Layer로 로직 위임
            String jwtToken = memberService.validateCode(email, code);

            // JWT를 URL 인코딩 및 쿠키 설정만 수행
            String encodedJwtToken = URLEncoder.encode(jwtToken, StandardCharsets.UTF_8);
            Cookie cookie = new Cookie("Authorization", encodedJwtToken);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);

            return CommonResponse.success("LOGIN_SUCCESS");
        } catch (RuntimeException ex) {
            log.error("Unauthorized error: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return CommonResponse.error("코드 인증 실패: " + ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            // 기타 예외 처리
            log.error("Internal server error: {}", ex.getMessage(), ex);
            return CommonResponse.error("서버 오류: " + ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }



    //현재 접속중인 유저 정보 호출
    @GetMapping("/info")
    public CommonResponse getUserInfo(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return CommonResponse.error("인증되지 않은 사용자입니다." , HttpStatus.UNAUTHORIZED.value());
            }

            // UserDetails에서 사용자 정보 가져오기
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Member member = memberService.findMemberByEmail(email);

            if (member != null) {
                return CommonResponse.success("USER_INFO_SUCCESS" , member);
            } else {
                return CommonResponse.error("해당 이메일의 회원 정보를 찾을 수 없습니다." , HttpStatus.BAD_REQUEST.value());
            }
        } catch (Exception e) {
            log.error("getUserInfo Error : {}", e.getMessage());
            return CommonResponse.error("서버 오류가 발생했습니다. : " + e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
    }



/*    //로그아웃 처리
    @PostMapping("/logout")
    public CommonResponse logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // OAuth2 토큰 제거
            if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken oauthtoken) {
                authorizedClientService.removeAuthorizedClient(
                        oauthtoken.getAuthorizedClientRegistrationId(),
                        oauthtoken.getName()
                );
            }

            // 세션 무효화 및 컨텍스트 제거
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            // JSESSIONID 쿠키 삭제
            Cookie sessionCookie = new Cookie("JSESSIONID", null);
            sessionCookie.setPath("/");
            sessionCookie.setHttpOnly(true);
            sessionCookie.setMaxAge(0); // 즉시 만료
            response.addCookie(sessionCookie);

            // JWT Authorization 쿠키 삭제
            Cookie jwtCookie = new Cookie("Authorization", null);
            jwtCookie.setPath("/"); // 쿠키 범위 설정
            jwtCookie.setHttpOnly(true); // 보안 설정
            jwtCookie.setMaxAge(0); // 쿠키 만료
            response.addCookie(jwtCookie);

            // 로그아웃 성공 응답
            return CommonResponse.success("LOGOUT_SUCCESS");
        } catch (Exception e) {
            log.error("LOGOUT_ERROR: {}", e.getMessage());
            return CommonResponse.error("LOGOUT_ERROR: " + e.getMessage());
        }
    }*/

}
