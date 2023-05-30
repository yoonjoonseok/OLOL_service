package com.ll.olol.service;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Transactional
public class CommentServiceTest {

    static CommentService commentService;
    static CommentRepository commentRepository;
    static RecruitmentRepository recruitmentRepository;
    static RecruitmentService recruitmentArticle;

    @Before
    static void before(){
        RecruitmentArticle recruitmentArticle1 = new RecruitmentArticle();
        recruitmentRepository.save(recruitmentArticle1);
    }
    @Test
    void 댓글_작성(){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setContent("하이요");
        commentService.commentSave(1L, commentDto);
        System.out.println(commentRepository.findById(1L));


    }
    @Test
    void 댓글_작성_예외_기능(){

        Assertions.assertThrows(NullPointerException.class, () ->{
            commentService.commentSave(2L, new CommentDto());
        });

    }
}
