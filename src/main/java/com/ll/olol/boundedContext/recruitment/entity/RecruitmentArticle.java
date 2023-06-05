package com.ll.olol.boundedContext.recruitment.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@EntityListeners(AuditingEntityListener.class)
public class RecruitmentArticle {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "RecruitmentArticle_ID")
    private Long id;
    @ManyToOne
    private Member member;
    private int typeValue;
    private String articleName;
    @CreatedDate
    private LocalDateTime createDate;
    private String content;
    private LocalDateTime deadLineDate;
    private Long views;

    @OneToMany(mappedBy = "recruitmentArticle", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id desc")
    private List<Comment> comment;

    @OneToOne(mappedBy = "recruitmentArticle")
    private RecruitmentArticleForm recruitmentArticleForm;

    @OneToMany(mappedBy = "recruitmentArticle")

    private List<RecruitmentPeople> recruitmentPeople;

    public String getTypeValueToString() {
        if (typeValue == 1) {
            return "정기";
        } else {
            return "번개";
        }
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
}
