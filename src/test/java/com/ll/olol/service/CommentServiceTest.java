package com.ll.olol.service;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Transactional
public class CommentServiceTest {

    CommentService commentService;
    RecruitmentRepository recruitmentRepository;
    RecruitmentService recruitmentArticle;
    @Test
    void 댓글_작성_예외_기능(){
        CommentDto commentDto = new CommentDto();
        commentDto.setRecruitmentArticle(recruitmentRepository.findBy());
        commentDto.setContent("안녕하세요");

        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            commentService.commentSave( commentDto.getId(), commentDto);
        });

    }
}
