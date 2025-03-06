package com.clone.rottentomato.domain.movie.component.entity;

import com.clone.rottentomato.common.component.entity.TimeStamped;
import com.clone.rottentomato.domain.movie.constant.ProducerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/** 영화 제작자 엔티티 */
@Getter
@Entity
@NoArgsConstructor
public class Producer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 영화 제작자 id
    @Column(nullable = false)
    private String name;    // 이름
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProducerType roleType; // 제작자 역할 타입

    private Producer(String name, ProducerType type){
        this.name = name;
        this.roleType = type;
    }

    public static Producer of(String name, ProducerType roleType){
        if(StringUtils.isBlank(name) || Objects.isNull(roleType)) return null;
        return new Producer(name, roleType);
    }

}
