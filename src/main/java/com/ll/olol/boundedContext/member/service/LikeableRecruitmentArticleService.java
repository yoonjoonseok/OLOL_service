package com.ll.olol.boundedContext.member.service;

import com.ll.olol.boundedContext.member.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.repository.LikeableRecruitmentArticleRepository;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class LikeableRecruitmentArticleService {
    private final LikeableRecruitmentArticleRepository likeableRecruitmentArticleRepository;

    @Transactional
    public void add(LikeableRecruitmentArticle likeableRecruitmentArticle) {
        likeableRecruitmentArticleRepository.save(likeableRecruitmentArticle);
    }

    @Transactional
    public void delete(LikeableRecruitmentArticle likeableRecruitmentArticle) {
        likeableRecruitmentArticleRepository.delete(likeableRecruitmentArticle);
    }

    public List<LikeableRecruitmentArticle> findByFromMember(Member FromMember) {
        return likeableRecruitmentArticleRepository.findAllByFromMember(FromMember);
    }

    public Optional<LikeableRecruitmentArticle> findById(Long id) {
        return likeableRecruitmentArticleRepository.findById(id);
    }

    public Optional<LikeableRecruitmentArticle> findByRecruitmentArticleAndFromMember(RecruitmentArticle recruitmentArticle, Member member) {
        return likeableRecruitmentArticleRepository.findByRecruitmentArticleAndFromMember(recruitmentArticle, member);
    }
}
