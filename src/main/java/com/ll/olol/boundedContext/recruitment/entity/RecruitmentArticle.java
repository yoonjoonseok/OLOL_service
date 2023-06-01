package com.ll.olol.boundedContext.recruitment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.*;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class RecruitmentArticle {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "RecruitmentArticle_ID")
    private Long id;
    @ManyToOne
    private Member member;
    private String title;
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
}
