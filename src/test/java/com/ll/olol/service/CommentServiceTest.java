package com.ll.olol.service;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;
    @Autowired
    private RecruitmentService recruitmentArticle;
    @Autowired
    private MemberRepository memberRepository;

    /**
     * member, comment, recruitment 데이터들이 initData 로 생성해서 4개씩 있습니다.
     */


    @Test
    public void 댓글_작성() {
        //given
        Comment comment = new Comment();
        comment.setContent("테스트댓글이에요.");
        //when
        commentRepository.save(comment);
        Optional<Comment> id = commentRepository.findById(comment.getId());
        //then
        assertEquals("테스트댓글이에요.", id.get().getContent());
    }

    @Test
    public void 댓글_삭제() {
        //given
        List<Comment> comments = commentService.findComments();
        Long id = comments.get(0).getId();
        //when
        commentRepository.delete(commentRepository.findById(id).get());
        //then
        assertEquals(3, commentService.findComments().size());
    }

}
