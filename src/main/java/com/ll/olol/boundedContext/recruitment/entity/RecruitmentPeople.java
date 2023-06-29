package com.ll.olol.boundedContext.recruitment.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.review.entity.ReviewMember;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@EntityListeners(AuditingEntityListener.class)
public class RecruitmentPeople {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "recruitmentArticle_id")
    private RecruitmentArticle recruitmentArticle;

    @OneToOne
    private ReviewMember reviewMember;

    private boolean isAttend;

    private boolean realParticipant;

    private boolean reviewComplete;

    public void checkedParticipant(boolean participant) {
        this.realParticipant = participant;
    }

    public boolean isRealParticipant() {
        return realParticipant;
    }

    public void checkReviewComplete(boolean complete) {
        this.reviewComplete = complete;
    }

    public boolean isReviewComplete() {
        return reviewComplete;
    }

}
