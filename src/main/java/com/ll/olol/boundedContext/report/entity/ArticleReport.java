package com.ll.olol.boundedContext.report.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
public class ArticleReport {
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

    private int reason;

    private int reportCount;

    public String getReasonToString() {
        if (reason == 1) {
            return "과도한 광고";
        } else if (reason == 2) {
            return "욕설";
        } else {
            return "부적절한 콘텐츠";
        }
    }

}
