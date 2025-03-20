package com.clone.rottentomato.domain.likes.component.entity;


import com.clone.rottentomato.domain.member.component.entity.Member;
import com.clone.rottentomato.domain.movie.component.entity.Movie;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Liked")
public class Likes {
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
    public Likes(Movie movie, Member member,int isStatus) {
        this.member = member;
        this.isStatus = isStatus;
        this.movie = movie;
    }


    public static Likes of(Movie movie,Member member,int isStatus) {
        return Likes.builder()
                .movie(movie)
                .member(member)
                .isStatus(isStatus)
                .build();
    }
}
