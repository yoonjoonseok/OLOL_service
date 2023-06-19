package com.ll.olol.boundedContext.recruitment.entity;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.CreateForm;
import com.ll.olol.boundedContext.report.entity.ArticleReport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@DynamicUpdate

public class RecruitmentArticle {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    private int typeValue;
    private String articleName;
    @CreatedDate
    private LocalDateTime createDate;

    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime deadLineDate;
    private Long views;

    @OneToMany(mappedBy = "recruitmentArticle", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id desc")
    private List<Comment> comment;

    @OneToMany(mappedBy = "recruitmentArticle", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ArticleReport> articleReports;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "recruitmentArticle")
    private RecruitmentArticleForm recruitmentArticleForm;

    @OneToMany(mappedBy = "recruitmentArticle", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<RecruitmentPeople> recruitmentPeople;

    @OneToMany(mappedBy = "recruitmentArticle", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<LikeableRecruitmentArticle> likeableRecruitmentArticles;

    public String getTypeValueToString() {
        if (typeValue == 1) {
            return "정기";
        } else {
            return "번개";
        }
    }

    public boolean isImpromotu() {
        return typeValue == 2;
    }

    public boolean isRegular() {
        return typeValue == 1;
    }

    public String getCreateDateToString() {
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    public String getDeadLineDateToString() {
        return deadLineDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public void addComment(Comment c) {
        c.setRecruitmentArticle(this); // 넌 나랑 관련된 답변이야.
        comment.add(c); // 너는 나랑 관련되어 있는 답변들 중 하나야.
    }

    public void update(CreateForm createForm) {
        this.typeValue = createForm.getTypeValue();
        this.articleName = createForm.getArticleName();
        this.content = createForm.getContent();
        this.deadLineDate = createForm.getDeadLineDate();
    }
}
