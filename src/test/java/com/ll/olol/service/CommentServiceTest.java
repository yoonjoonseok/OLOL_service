package com.ll.olol.service;

import static org.junit.Assert.assertEquals;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import java.util.List;
import org.junit.Before;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@RunWith(JUnit4.class)
@Transactional
public class CommentServiceTest {
    @Autowired
    static CommentService commentService;
    @Autowired
    static CommentRepository commentRepository;
    @Autowired
    static RecruitmentRepository recruitmentRepository;
    @Autowired
    static RecruitmentService recruitmentArticle;

    @Test
    public void 댓글_작성(){
        List<RecruitmentArticle> all = recruitmentRepository.findAll();

        assertEquals(4,all.size());

    }

}
