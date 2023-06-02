package com.ll.olol.boundedContext.comment.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruitmentArticle_id")
    private RecruitmentArticle recruitmentArticle;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private String content;

    @CreatedDate
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

}