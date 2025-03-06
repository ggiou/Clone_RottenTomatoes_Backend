package com.clone.rottentomato.domain.mypage.controller;

import com.clone.rottentomato.domain.auth.component.UserDetailsImpl;
import com.clone.rottentomato.domain.mypage.component.dto.MypageMovieResponseDto;
import com.clone.rottentomato.domain.mypage.component.dto.MypageResponseDto;
import com.clone.rottentomato.domain.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;


    //  유저 정보 조회
    @GetMapping("/profiles")
    public ResponseEntity<MypageResponseDto>getMembers(UserDetailsImpl userDetails) {
        return mypageService.getMembers(userDetails.getMember());
    }


    //  좋아요 리스트
    @GetMapping("/watch_list")
    public ResponseEntity<List<MypageMovieResponseDto>> getMovies(@RequestParam(value = "page")int page,
                                                                  @RequestParam(value = "size")int size,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getLikes(page,size,userDetails.getMember());
    }
}
