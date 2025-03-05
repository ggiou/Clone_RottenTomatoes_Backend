package com.clone.rottentomato.domain.mypage.service;


import com.clone.rottentomato.domain.likes.component.entity.Likes;
import com.clone.rottentomato.domain.likes.repository.LikesRepository;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import com.clone.rottentomato.domain.mypage.component.dto.MypageMovieResponseDto;
import com.clone.rottentomato.domain.mypage.component.dto.MypageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MypageService {

    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;


    //  회원 정보 조회
    @Transactional(readOnly = true)
    public ResponseEntity<MypageResponseDto> getMembers(Member member) {
        Optional<Member> find = memberRepository.findByMemberEmail(member.getMemberEmail());
        if(find.isEmpty()){
            throw new IllegalArgumentException("회원이 아닙니다.");
        }
        MypageResponseDto responseDto = MypageResponseDto.of(find.get());
        return ResponseEntity.ok(responseDto);
    }


    //  좋아요 리스트
    @Transactional(readOnly = true)
    public ResponseEntity<List<MypageMovieResponseDto>> getLikes(int page, int size, Member member) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Likes> likespage = likesRepository.findByAndMemberEmail(pageable, member.getMemberEmail());
        if(likespage.isEmpty()){
            throw new IllegalArgumentException("찜한 내역이 없습니다.");
        }
        MypageMovieResponseDto responseDto = MypageMovieResponseDto.from((Likes) likespage.get());
        return ResponseEntity.ok(Collections.singletonList(responseDto));
    }




}
