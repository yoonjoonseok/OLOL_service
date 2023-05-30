package com.ll.olol.boundedContext.comment.service;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.comment.entity.CommentDto;
import com.ll.olol.boundedContext.comment.repository.CommentRepository;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public Long commentSave(String name, Long id, CommentDto dto){
        RecruitmentArticle recruitmentArticle = recruitmentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        dto.setRecruitmentArticle(recruitmentArticle);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return dto.getId();
    }
}
