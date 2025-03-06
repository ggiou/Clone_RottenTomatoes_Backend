package com.clone.rottentomato.domain.mypage.component.dto;


import com.clone.rottentomato.domain.member.component.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MypageResponseDto {
    private Long id;
    private String memberEmail;
    private String memberName;


    @Builder
    public MypageResponseDto(Member member) {
        this.id = member.getMemberId();
        this.memberEmail = member.getMemberEmail();
        this.memberName = member.getMemberName();
    }


    public static MypageResponseDto of(Member member){
        return MypageResponseDto.builder()
                .member(member)
                .build();
    }
}
