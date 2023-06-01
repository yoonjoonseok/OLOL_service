package com.ll.olol.boundedContext.recruitment.entity;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private List<Comment> comment;

    @OneToOne(mappedBy = "recruitmentArticle")
    private RecruitmentArticleForm recruitmentArticleForm;

    public String getTypeValueToString() {
        if (typeValue == 1)
            return "정기";
        else
            return "번개";
    }

    public String getCreateDateToString() {
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    public String getDeadLineDateToString() {
        return deadLineDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}
