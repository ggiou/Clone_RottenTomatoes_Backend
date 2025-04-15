package com.clone.rottentomato.domain.saved.controller;


import com.clone.rottentomato.common.component.dto.CommonResponse;
import com.clone.rottentomato.domain.auth.component.UserDetailsImpl;
import com.clone.rottentomato.domain.saved.service.SavedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("save")
public class SavedController {

    private final SavedService savedService;


    @PostMapping("/{movie_id}/save")
    public CommonResponse save(@PathVariable Long movie_id,
                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                               @RequestParam(defaultValue = "0") int isStatus) {
        return savedService.save(movie_id,userDetails.getMember(),isStatus);
    }


    @GetMapping("/check")
    public CommonResponse check(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestParam Long movie_id) {
        return savedService.check(userDetails.getMember(),movie_id);
    }

}
