package com.ll.olol.boundedContext.notification.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
public class Notification {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    private int type;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime readDate;

    private String content;

    public boolean isRead() {
        return readDate != null;
    }

    public void markAsRead() {
        readDate = LocalDateTime.now();
    }
}