package com.ll.olol.boundedContext.recruitment.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @Column(name = "RecruitmentPeople_ID")
    private Long id;
    @ManyToOne
    private Member member;

    @ManyToOne
    private RecruitmentArticle recruitmentArticle;

}
