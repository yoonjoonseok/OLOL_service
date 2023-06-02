package com.ll.olol.boundedContext.member.service;

import com.ll.olol.boundedContext.member.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.LikeableRecruitmentArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class LikeableRecruitmentArticleService {
    private final LikeableRecruitmentArticleRepository likeableRecruitmentArticleRepository;

    public List<LikeableRecruitmentArticle> findByFromMember(Member FromMember) {
        return likeableRecruitmentArticleRepository.findAllByFromMember(FromMember);
    }
}
