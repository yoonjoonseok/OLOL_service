package com.ll.olol.boundedContext.comment.service;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.MemberRepository;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
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
    public RsData commentSave(Comment comment, String writer, Long articleId) {
        Member member = rq.getMember();
        member.setNickname(writer);
        memberRepository.save(member);

        Optional<RecruitmentArticle> article = recruitmentRepository.findById(articleId);
        if (article.isEmpty()) {
            return RsData.of("F-1", "게시물이 없습니다.");
        }
        Comment savedComment = new Comment();
        savedComment.setContent(comment.getContent());
        savedComment.setRecruitmentArticle(article.get());
        savedComment.setMember(member);

        Comment save = commentRepository.save(savedComment);
        return RsData.of("S-1", "댓글 작성 성공");
    }

    public List<Comment> findComments() {
        return commentRepository.findAll();
    }

    public Comment findOne(Long id) {
        return commentRepository.findById(id).get();
    }

    @Transactional
    public Comment update(Long id, String content) {
        Comment updateComment = findOne(id);
        updateComment.setContent(content);
        return updateComment;
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
