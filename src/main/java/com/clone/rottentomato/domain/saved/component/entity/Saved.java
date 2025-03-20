package com.clone.rottentomato.domain.saved.component.entity;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import com.clone.rottentomato.domain.saved.component.dto.SavedRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Saved {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int isStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID",nullable = false)
    private Movie movie;


    @Builder
    public Saved(Movie movie, Member member, SavedRequestDto requestDto) {
        this.member = member;
        this.movie = movie;
        this.isStatus = requestDto.getIsStatus();
    }


    public static Saved of(Movie movie, Member member,SavedRequestDto requestDto) {
        return Saved.builder()
                .movie(movie)
                .member(member)
                .requestDto(requestDto)
                .build();
    }
}
