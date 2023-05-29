package com.ll.olol.boundedContext.member.entity;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class RecruitmentApplyMember {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
    @Setter
    private RecruitmentArticle recruitmentArticle;

    @ManyToOne
    private Member fromMember;

    private boolean isAccepted;


}
