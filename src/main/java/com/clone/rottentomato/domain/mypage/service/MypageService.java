package com.clone.rottentomato.domain.mypage.service;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import com.clone.rottentomato.domain.mypage.component.dto.MypageMovieResponseDto;
import com.clone.rottentomato.domain.mypage.component.dto.MypageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MypageService {

    private final MemberRepository memberRepository;

    //  회원 정보 조회
    public ResponseEntity<MypageResponseDto> getMembers(String email) {
        Optional<Member> find = memberRepository.findByMemberEmails(email);
        if(find.isEmpty()){
            throw new IllegalArgumentException("회원이 아닙니다.");
        }
        MypageResponseDto responseDto = MypageResponseDto.of(find.get());
        return ResponseEntity.ok(responseDto);
    }

    public ResponseEntity<MypageMovieResponseDto> getMovies() {
        return null;
    }
}
