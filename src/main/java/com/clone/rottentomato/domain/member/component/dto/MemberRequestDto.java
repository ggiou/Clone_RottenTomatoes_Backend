package com.clone.rottentomato.domain.member.component.dto;

import lombok.Getter;

@Getter
public class MemberRequestDto {
    private String email;
    private String memberName;
    private String authCode;

    public MemberRequestDto(String email, String memberName, String authCode) {
        this.email = email;
        this.memberName = memberName;
        this.authCode = authCode;
    }
}
