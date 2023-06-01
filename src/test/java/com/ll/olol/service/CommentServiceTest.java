package com.ll.olol.service;

import static org.junit.Assert.assertEquals;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.comment.service.CommentService;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
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
    @Autowired
    static MemberRepository memberRepository;

    @Test
    public void 댓글_작성() {
        List<Member> members = memberRepository.findAll();
        List<Comment> all = commentRepository.findAll();
        Member member = members.get(0);
        Comment comment = new Comment();

        comment.setMember(member);
        comment.setContent("테스트댓글입니다");

        assertEquals(5, all.size());
    }

}
