package com.clone.rottentomato.domain.likes.controller;


import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.domain.auth.component.UserDetailsImpl;
import com.clone.rottentomato.domain.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("likes")
public class LikesController {

    private final LikesService likesService;

    //  좋아요
    @PostMapping("/{movie_id}/isLiked")
    public CommonResponse ok(@PathVariable Long movie_id,
                             @AuthenticationPrincipal UserDetailsImpl userDetails,
                             @RequestParam(defaultValue = "0") int isStatus) {
        return likesService.ok(movie_id,userDetails.getMember(),isStatus);
    }


}
