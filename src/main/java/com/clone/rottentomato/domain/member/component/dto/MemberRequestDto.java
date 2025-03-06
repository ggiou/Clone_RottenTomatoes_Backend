package com.clone.rottentomato.domain.member.component.dto;

import lombok.Getter;

@Getter
public class MemberRequestDto {
    private String email;
    private String authCode;

    public MemberRequestDto(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
    }
}
