package com.clone.rottentomato.common.component.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimeStamped {
    //TODO 2024-12-12 11:26:03.515593	 이형식으로 되서 형식 바꿔 저장할 수 있는지 확인 필요
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime modDate;

}
