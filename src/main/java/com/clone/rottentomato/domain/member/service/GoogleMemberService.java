package com.clone.rottentomato.domain.member.service;

import com.clone.rottentomato.config.GoogleConfig;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleMemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final GoogleConfig googleConfig;



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
