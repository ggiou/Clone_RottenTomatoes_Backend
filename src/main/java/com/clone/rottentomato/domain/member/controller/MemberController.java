package com.clone.rottentomato.domain.member.controller;

import com.clone.rottentomato.domain.member.component.dto.MemberRequestDto;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import com.clone.rottentomato.domain.member.service.GoogleMemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final GoogleMemberService googleMemberService;

    @GetMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody MemberRequestDto memberRequestDto , HttpServletResponse response){
        googleMemberService.handleLogin(memberRequestDto);
        return ResponseEntity.ok().build();
    }

}
