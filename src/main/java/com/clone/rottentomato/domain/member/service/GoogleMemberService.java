package com.clone.rottentomato.domain.member.service;

import com.clone.rottentomato.config.GoogleConfig;
import com.clone.rottentomato.domain.member.component.dto.MemberRequestDto;
import com.clone.rottentomato.domain.member.component.entity.GoogleMember;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GoogleMemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final GoogleConfig googleConfig;


/*    public GoogleMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/


    public String handleLogin(MemberRequestDto requestDto) {
        String email = requestDto.getEmail();
        // 1. 이메일 형식 검증
        if (!isValidEmailFormat(email)) {
            return "잘못된 이메일 형식입니다.";
        }

        // 2. 이메일 등록 여부 확인
        Member member;
        if (isExistMember(email)) {
            // 2-1. 이메일 미등록 상태, 새로운 계정 생성
            String provider = isGoogleEmail(email) ? "GOOGLE" : "LOCAL";
            member = registerMember(email, provider);
        } else {
            // 2-2. 등록된 계정 정보 가져오기
            member = memberRepository.findByMemberEmail(email).stream().findFirst().orElse(null);
        }

        // 3. 이메일이 구글 계정 형식인지 확인
        if ("GOOGLE".equalsIgnoreCase(member.getProvider())) {
            return getGoogleAuthorizationUrl(email);
        } else {
            // authCode 생성 및 업데이트 (일반 사용자)
            String newAuthCode = generateAuthCode();
            member.updateAuthCode(newAuthCode);
            memberRepository.save(member);
            return "일반 로그인 진행: 인증 코드가 발급되었습니다."; // 필요 시 추가 응답 수정
        }
    }


    private String getGoogleAuthorizationUrl(String email) {
        // 리다이렉트 URL 등 관련된 Google 인증 로직 구현
        return "https://accounts.google.com/o/oauth2/auth?" +
                "scope=email%20profile&" +
                "response_type=code&" +
                "redirect_uri="+ googleConfig.getRedirectUri()+"&" +
                "client_id="+googleConfig.getClientId();
    }



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = ((DefaultOAuth2User) oAuth2User).getAttribute("email");
        String name = ((DefaultOAuth2User) oAuth2User).getAttribute("name");

        return new GoogleMember(oAuth2User);
    }


    private Member registerMember(String email, String provider) {
        Member newMember = new Member(
        email,
        email.split("@")[0], // 이메일의 아이디 부분을 이름으로 대체
        provider,
                "NEWCODE");
        return memberRepository.save(newMember); // 새 계정 저장 후 반환
    }


    //유저 등록 확인 (중복 방지)
    public Boolean isExistMember(String email){
        return memberRepository.findByMemberEmail(email).stream().findFirst().isPresent();
    }

    //신규 유저 저장
    public void SaveMember(Member member){
        memberRepository.save(member);
    }

    public boolean isGoogleEmail(String email) {
        return email.endsWith("@gmail.com"); // Gmail (Google 계정 형식)
    }

    public boolean isValidEmailFormat(String email) {
        if (StringUtils.isEmpty(email)) return false;
        // 이메일 형식 검증 정규식
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }

    private String generateAuthCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8); // 8자리 랜덤 코드 생성
    }


}
