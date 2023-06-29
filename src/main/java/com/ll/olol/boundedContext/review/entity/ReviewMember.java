package com.ll.olol.boundedContext.review.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@ToString
public class ReviewMember {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private Member reviewMember;

    @ManyToOne
    private RecruitmentArticle recruitmentArticle;

    @OneToOne
    private RecruitmentPeople recruitmentPeople;

    public void updateRecruitmentPeople(RecruitmentPeople recruitmentPeople) {
        this.recruitmentPeople = recruitmentPeople;
    }
}
