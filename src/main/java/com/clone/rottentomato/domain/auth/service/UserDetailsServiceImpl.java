package com.clone.rottentomato.domain.auth.service;

import com.clone.rottentomato.common.constant.CommonError;
import com.clone.rottentomato.common.constant.CustomError;
import com.clone.rottentomato.domain.auth.component.UserDetailsImpl;
import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.member.repository.MemberRepository;
import com.clone.rottentomato.domain.member.service.MemberService;
import com.clone.rottentomato.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        // 데이터베이스에서 사용자 검색
        Member findMember = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new CommonException("등록된 사용자를 찾을 수 없습니다." , "NOT_FOUND_MEMBER" , CommonError.NOT_FOUND)); // 미존재 사용자 처리
        // UserDetails 객체 반환
        return new UserDetailsImpl(findMember, findMember.getMemberEmail());
    }

}
