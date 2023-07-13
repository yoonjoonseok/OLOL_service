package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.LikeableRecruitmentArticleRepository;
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
    public RsData add(RecruitmentArticle recruitmentArticle, Member actor) {
        LikeableRecruitmentArticle likeableRecruitmentArticle = LikeableRecruitmentArticle
                .builder()
                .recruitmentArticle(recruitmentArticle)
                .fromMember(actor)
                .build();

        likeableRecruitmentArticleRepository.save(likeableRecruitmentArticle);
        return RsData.of("S-1", "찜 성공");
    }

    public RsData canAdd(Optional<RecruitmentArticle> recruitmentArticle, Member actor) {
        if (recruitmentArticle.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 모임 공고입니다");
        }

        Optional<LikeableRecruitmentArticle> likeableRecruitmentArticle = likeableRecruitmentArticleRepository.findByRecruitmentArticleAndFromMember(
                recruitmentArticle.get(), actor);

        if (likeableRecruitmentArticle.isPresent()) {
            return RsData.of("F-2", "이미 찜한 모임 공고입니다");
        }

        return RsData.of("S-1", "찜 가능한 모임 공고입니다");
    }

    @Transactional
    public RsData cancel(LikeableRecruitmentArticle likeableRecruitmentArticle) {
        likeableRecruitmentArticleRepository.delete(likeableRecruitmentArticle);
        return RsData.of("S-1", "찜 삭제 성공");
    }

    public RsData canCancel(Optional<LikeableRecruitmentArticle> likeableRecruitmentArticle, Member actor) {
        if (likeableRecruitmentArticle.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 찜입니다");
        }

        if (actor.getId() != likeableRecruitmentArticle.get().getFromMember().getId()) {
            return RsData.of("F-2", "사용자가 일치하지 않습니다");
        }

        return RsData.of("S-1", "찜 취소 가능한 모임 공고입니다");
    }

    public List<LikeableRecruitmentArticle> findByFromMemberOrderByIdDesc(Member FromMember) {
        return likeableRecruitmentArticleRepository.findByFromMemberOrderByIdDesc(FromMember);
    }

    public Optional<LikeableRecruitmentArticle> findById(Long id) {
        return likeableRecruitmentArticleRepository.findById(id);
    }
}
