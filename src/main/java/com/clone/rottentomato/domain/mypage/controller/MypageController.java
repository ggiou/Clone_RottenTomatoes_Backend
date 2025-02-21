package com.clone.rottentomato.domain.mypage.controller;

import com.clone.rottentomato.domain.mypage.component.dto.MypageMovieResponseDto;
import com.clone.rottentomato.domain.mypage.component.dto.MypageResponseDto;
import com.clone.rottentomato.domain.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;


    //  유저 정보 조회
    @GetMapping("/profiles")
    public ResponseEntity<MypageResponseDto>getMembers(String email){
        return mypageService.getMembers(email);
    }


    //  좋아요 리스트
    @GetMapping("/watch_list")
    public ResponseEntity<MypageMovieResponseDto>getMovies(){
        return mypageService.getMovies();
    }
}
