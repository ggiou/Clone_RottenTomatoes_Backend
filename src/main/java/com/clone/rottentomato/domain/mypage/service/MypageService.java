package com.clone.rottentomato.domain.mypage.service;


import com.clone.rottentomato.domain.likes.component.dto.LikesPageResponseDto;
import com.clone.rottentomato.domain.likes.component.entity.Likes;
import com.clone.rottentomato.domain.likes.repository.LikesRepository;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import com.clone.rottentomato.domain.mypage.component.dto.MypageMovieResponseDto;
import com.clone.rottentomato.domain.mypage.component.dto.MypageMovieSaveResponseDto;
import com.clone.rottentomato.domain.mypage.component.dto.MypageResponseDto;
import com.clone.rottentomato.domain.saved.component.dto.SavedPageResponseDto;
import com.clone.rottentomato.domain.saved.component.entity.Saved;
import com.clone.rottentomato.domain.saved.repository.SavedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MypageService {

    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final SavedRepository savedRepository;


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
    public ResponseEntity<LikesPageResponseDto> getLikes(int page, int size, Member member) {
        Pageable pageable = getPageable(page, size);
        Page<Likes> likesPage = likesRepository.findByMemberEmail(member.getMemberEmail(), pageable);
        if(likesPage.isEmpty()){
            throw new NotFoundException("좋아요 내역이 없습니다.");
        }
        List<MypageMovieResponseDto> content = likesPage.getContent().stream()
                .map(MypageMovieResponseDto::from)
                .collect(Collectors.toList());
        int totalCount = (int) likesPage.getTotalElements();
        int totalPage = (int) Math.ceil((double) totalCount / 12); // ✅ 고정 사이즈 12 기준
        LikesPageResponseDto responseDto = LikesPageResponseDto.from(content, totalPage);
        return ResponseEntity.ok(responseDto);
    }


    //  저장하기 리스트
    @Transactional(readOnly = true)
    public ResponseEntity<SavedPageResponseDto> getSave(int page, int size, Member member) {
        Pageable pageable = getPageable(page, size);
        Page<Saved> savedPage = savedRepository.findByMemberEmail(member.getMemberEmail(), pageable);
        if(savedPage.isEmpty()){
            throw new NotFoundException("저장한 내역이 없습니다.");
        }
        List<MypageMovieSaveResponseDto> content = savedPage.getContent().stream()
                .map(MypageMovieSaveResponseDto::from)
                .collect(Collectors.toList());
        int totalCount = (int) savedPage.getTotalElements();
        int totalPage = (int) Math.ceil((double) totalCount / 12); // ✅ 고정 사이즈 12 기준
        SavedPageResponseDto responseDto = SavedPageResponseDto.from(content, totalPage);
        return ResponseEntity.ok(responseDto);
    }


    //      메서드
    private static Pageable getPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by("createdAt").descending());
    }
}
