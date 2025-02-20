package com.clone.rottentomato.domain.likes.component.entity;


import com.clone.rottentomato.domain.movie.component.entity.Movie;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Likes {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBER_ID",nullable = false)
//    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID",nullable = false)
    private Movie movie;


    @Builder
    public Likes( Movie movie) {
//        this.member = member;
        this.movie = movie;
    }


    public static Likes of( Movie movie) {
        return Likes.builder()
                .movie(movie)
                .build();
    }
}
