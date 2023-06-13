package com.ll.olol.boundedContext.comment.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private RecruitmentArticle recruitmentArticle;
    private Member member;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createDate = LocalDateTime.now();
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifyDate = LocalDateTime.now();

    public Comment toEntity() {
        Comment comments = Comment.builder()
                .id(id)
                .content(content)
                .recruitmentArticle(recruitmentArticle)
                .createDate(createDate)
                .modifyDate(modifyDate)
                .build();
        return comments;
    }
}