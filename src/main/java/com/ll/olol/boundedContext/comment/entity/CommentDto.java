package com.ll.olol.boundedContext.comment.entity;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private RecruitmentArticle recruitmentArticle;
    private Member member;
    private String content;
    private String writer;
    private String password;
    private LocalDateTime createDate = LocalDateTime.now();
    private LocalDateTime modifyDate = LocalDateTime.now();

//    public Comment toEntity(){
//        Comment comments = Comment.builder()
//                .id(id)
//                .content(content)
//                .recruitmentArticle(recruitmentArticle)
//                .createDate(createDate)
//                .modifyDate(modifyDate)
//                .build();
//        return comments;
//    }
}