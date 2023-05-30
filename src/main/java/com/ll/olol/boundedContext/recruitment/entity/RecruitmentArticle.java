package com.ll.olol.boundedContext.recruitment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.olol.boundedContext.comment.entity.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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
    private Long id;

    private String articleName;
    @CreatedDate
    private LocalDateTime createDate;

    private String content;
    @OneToMany(mappedBy = "recruitmentArticle",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id desc")
    private List<Comment> comment;

}
