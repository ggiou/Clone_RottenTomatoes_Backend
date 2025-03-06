package com.clone.rottentomato.domain.member.service;

import com.clone.rottentomato.config.GoogleConfig;
import com.clone.rottentomato.domain.member.component.dto.MemberRequestDto;
import com.clone.rottentomato.domain.member.component.entity.GoogleMember;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;
import java.util.Optional;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleMemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final GoogleConfig googleConfig;



    public String handleLogin(MemberRequestDto requestDto) {
        String email = requestDto.getEmail();
        // 1. 이메일 형식 검증
        if (!memberService.isValidEmailFormat(email)) {
            return "잘못된 이메일 형식입니다.";
        }

        // 2. 이메일 등록 여부 확인
        Member member;
        if (!memberService.isExistMember(email)) {
            // 2-1. 이메일 미등록 상태, 새로운 계정 생성
            String provider = memberService.isGoogleEmail(email) ? "GOOGLE" : "LOCAL";
            member = memberService.registerMember(email, provider);
        } else {
            // 2-2. 등록된 계정 정보 가져오기
            member = memberRepository.findByMemberEmail(email).stream().findFirst().orElse(null);
        }

        // 3. 이메일이 구글 계정 형식인지 확인
        if ("GOOGLE".equalsIgnoreCase(member.getProvider())) {
            return getGoogleAuthorizationUrl();
        } else {
            // authCode 생성 및 업데이트 (일반 사용자)
            String newAuthCode = memberService.generateAuthCode();
            member.updateAuthCode(newAuthCode);
            memberRepository.save(member);

            //인증코드 메일 발송 추가.

            return "일반 로그인 진행: 인증 코드가 발급되었습니다."; // 필요 시 추가 응답 수정
        }
    }


    public String getGoogleAuthorizationUrl() {
        // 리다이렉트 URL 등 관련된 Google 인증 로직 구현
        return "https://accounts.google.com/o/oauth2/auth?" +
                "scope=email profile&" +
                "response_type=code&" +
                "redirect_uri="+ googleConfig.getRedirectUri()+"&" +
                "client_id="+googleConfig.getClientId();
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 액세스 토큰 확인 (디버깅용)
        String accessToken = userRequest.getAccessToken().getTokenValue();
        log.info("------------- loadUser -------------  ");
        System.out.println("액세스 토큰: " + accessToken);

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("유저 이메일: " + email);
        log.info("유저 이름: " + name);

        // 데이터베이스에 저장 (이미 존재하는 경우 가져옴)
        Optional<Member> existingMember = memberRepository.findByMemberEmail(email);
        Member member = existingMember.orElseGet(() -> memberService.registerGoogleMember(email, name,"GOOGLE"));

        return oAuth2User;
    }
}
