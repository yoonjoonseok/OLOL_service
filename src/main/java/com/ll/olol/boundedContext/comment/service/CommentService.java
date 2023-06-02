package com.ll.olol.boundedContext.comment.service;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
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
    @Autowired
    private final Rq rq;
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
    public Long commentSave(Comment comment, String writer) {

        Member member = rq.getMember();
        member.setNickname(writer);
        memberRepository.save(member);
        comment.setMember(member);

        Comment save = commentRepository.save(comment);
        return save.getId();
    }

    public List<Comment> findComments() {
        return commentRepository.findAll();
    }

    public Comment findOne(Long id) {
        return commentRepository.findById(id).get();
    }

    public Comment update(Long commentId, String content) {
        Comment updateComment = findOne(commentId);
        updateComment.setContent(content);
        return commentRepository.save(updateComment);
    }

    @Transactional
    public void commentDelete(Long id) {
        Optional<Comment> id1 = commentRepository.findById(id);

        commentRepository.delete(id1.get());
    }

    public RsData isEqualMemberById(Long id) {
        if (rq.getMember().getId() != findOne(id).getMember().getId()) {
            return RsData.of("F-1", "동일한 사용자만 할 수 있습니다.");
        }
        return RsData.of("S-1", "성공");
    }
}
