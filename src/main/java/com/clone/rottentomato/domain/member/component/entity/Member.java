package com.clone.rottentomato.domain.member.component.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.boot.model.internal.Nullability;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long memberId;

    @Column( nullable = false )
    private String memberEmail;

    @Column( nullable = false )
    private String memberName;

    @Column( nullable = false )
    private String authCode;



    public Member(String memberEmail, String memberName, String authCode) {
        this.memberEmail = memberEmail;
        this.memberName = memberName;
        this.authCode = authCode;
    }
}
