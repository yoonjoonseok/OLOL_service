package com.ll.olol.boundedContext.recruitment.entity;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class RecruitmentArticle {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "RecruitmentArticle_ID")
    private Long id;
    @ManyToOne
    private Member member;

    private String title;

    private String content;

    private int typeValue;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime deadLineDate;

    private Long views;

    @OneToMany(mappedBy = "recruitmentArticle", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<Comment> commentList = new ArrayList<>();

    @OneToOne(mappedBy = "recruitmentArticle")
    private RecruitmentArticleForm recruitmentArticleForm;

    public void addComment(Comment c) {
        c.setRecruitmentArticle(this); // 넌 나랑 관련된 답변이야.
        commentList.add(c); // 너는 나랑 관련되어 있는 답변들 중 하나야.
    }
}
