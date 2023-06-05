package com.ll.olol.boundedContext.member.service;

import com.ll.olol.base.rsData.RsData;
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
    public void add(RecruitmentArticle recruitmentArticle, Member actor) {
        LikeableRecruitmentArticle likeableRecruitmentArticle = LikeableRecruitmentArticle
                .builder()
                .recruitmentArticle(recruitmentArticle)
                .fromMember(actor)
                .build();

        likeableRecruitmentArticleRepository.save(likeableRecruitmentArticle);
    }

    public RsData canAdd(Optional<RecruitmentArticle> recruitmentArticle, Member actor) {
        if (recruitmentArticle.isEmpty())
            return RsData.of("F-1", "존재하지 않는 모임 공고입니다");

        Optional<LikeableRecruitmentArticle> likeableRecruitmentArticle = likeableRecruitmentArticleRepository.findByRecruitmentArticleAndFromMember(recruitmentArticle.get(), actor);

        if (likeableRecruitmentArticle.isPresent())
            return RsData.of("F-2", "이미 찜한 모임 공고입니다");

        return RsData.of("S-1", "찜 가능한 모임 공고입니다");
    }

    @Transactional
    public void cancel(LikeableRecruitmentArticle likeableRecruitmentArticle) {
        likeableRecruitmentArticleRepository.delete(likeableRecruitmentArticle);
    }

    public RsData canCancel(Optional<LikeableRecruitmentArticle> likeableRecruitmentArticle, Member actor) {
        if (likeableRecruitmentArticle.isEmpty())
            return RsData.of("F-1", "존재하지 않는 찜입니다");

        if (actor.getId() != likeableRecruitmentArticle.get().getFromMember().getId())
            return RsData.of("F-2", "사용자가 일치하지 않습니다");

        return RsData.of("S-1", "찜 취소가 가능합니다");
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
