package com.ll.olol.boundedContext.comment.service;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final RecruitmentRepository recruitmentRepository;
    @Autowired
    private final MemberRepository memberRepository;

//    @Transactional
//    public Long commentSave(Long id, CommentDto dto){
//        RecruitmentArticle recruitmentArticle = recruitmentRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
//
////        dto.setRecruitmentArticle(recruitmentArticle);
//
//        Comment comment = dto.toEntity();
//        commentRepository.save(comment);
//
//        return dto.getId();
//    }
    @Transactional
    public Long commentSave(Comment comment, Member member, Long recruitmentId){
        Optional<Member> member1 = memberRepository.findById(member.getId());
        Optional<RecruitmentArticle> id = recruitmentRepository.findById(recruitmentId);

        comment.setMember(member1.get());
        comment.setRecruitmentArticle(id.get());
        Comment save = commentRepository.save(comment);
        return save.getId();
    }
    public List<Comment> findComments(){
        return commentRepository.findAll();
    }

    @Transactional
    public void commentDelete(Comment comment){
        commentRepository.delete(comment);
    }
}
