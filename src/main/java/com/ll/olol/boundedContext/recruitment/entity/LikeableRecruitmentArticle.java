package com.ll.olol.boundedContext.recruitment.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@ToString
public class LikeableRecruitmentArticle {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
    @Setter //
    private RecruitmentArticle recruitmentArticle;

    @ManyToOne
    private Member fromMember;


}
