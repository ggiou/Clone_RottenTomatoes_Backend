package com.clone.rottentomato.domain.member.service;

import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.BooleanDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //신규유저 등록 ( 일반)
    @Transactional
    public Member registerMember(String email, String provider) {
        Member newMember = new Member(
                email,
                email.split("@")[0], // 이메일의 아이디 부분을 이름으로 대체
                provider,
                generateAuthCode());

        log.info(" [일반]유저 추가 : " + newMember.getMemberEmail());
        return memberRepository.save(newMember); // 새 계정 저장 후 반환
    }

    //신규유저 등록 ( 구글)
    @Transactional
    public Member registerGoogleMember(String email,  String name, String provider) {
        Member newMember = new Member(
                email,
                name,
                provider,
                generateAuthCode());

        log.info(" [구글]유저 추가 : " + newMember.getMemberEmail());
        return memberRepository.save(newMember); // 새 계정 저장 후 반환
    }

    @Transactional
    public void updateAuthCode(Member member) {
        String authCode = generateAuthCode();
        member.updateAuthCode(authCode);
        memberRepository.save(member);
    }



    //유저 등록 확인 (중복 방지)
    @Transactional(readOnly = true)
    public Boolean isExistMember(String email){
        Boolean result = false;
        result = memberRepository.findByMemberEmail(email).stream().findFirst().isPresent();
        return result;
    }

    //유저정보 조회 - 이메일 검색
    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        return memberRepository.findByMemberEmail(email).stream().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public String getAuthCodeByEmail(String email) {
        return memberRepository.findByMemberEmail(email)
                .filter(member -> member.getAuthCode() != null)
                .map(Member::getAuthCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 또는 authCode가 없습니다."));
    }



    //구글 메일  검증
    public boolean isGoogleEmail(String email) {
        return email.endsWith("@gmail.com"); // Gmail (Google 계정 형식)
    }

    //이메일 형식 검증
    public boolean isValidEmailFormat(String email) {
        if (StringUtils.isEmpty(email)) return false;
        // 이메일 형식 검증 정규식
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }

    // 8자리 랜덤 코드 생성
    public String generateAuthCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);

    }
}
