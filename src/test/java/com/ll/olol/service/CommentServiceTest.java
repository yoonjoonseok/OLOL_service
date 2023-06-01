package com.ll.olol.service;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@Transactional
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

    @Test
    public void 댓글_작성() {
        List<Member> members = memberRepository.findAll();
        Member member = members.get(0);

        Comment comment = new Comment();

        comment.setMember(member);
        comment.setContent("테스트댓글입니다");
        commentService.commentSave(comment, "재성박");

        List<Comment> all = commentRepository.findAll();

        assertEquals(5, all.size());
    }

}
