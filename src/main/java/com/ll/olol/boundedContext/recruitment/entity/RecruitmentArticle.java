package com.ll.olol.boundedContext.recruitment.entity;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.report.entity.ArticleReport;
import com.ll.olol.boundedContext.review.entity.Review;
import com.ll.olol.boundedContext.review.entity.ReviewMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

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
    private boolean isDeadLine;

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

    @OneToMany(mappedBy = "recruitmentArticle", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "recruitmentArticle", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewMember> reviewMemberList;

    private boolean isEventTriggered;

    private boolean isCourseTimeEnd;

    public String getTypeValueToString() {
        if (typeValue == 1) {
            return "번개";
        } else {
            return "정기";
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

    public Long getAttend() {
        return recruitmentPeople.stream()
                .filter(t -> t.isAttend())
                .count();
    }

    public String getLocalDateTimeTextStyleDay() {
        DayOfWeek dayOfWeek = deadLineDate.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREA);
    }


}
