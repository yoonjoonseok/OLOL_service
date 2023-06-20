package com.ll.olol.boundedContext.recruitment.repository;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeableRecruitmentArticleRepository extends JpaRepository<LikeableRecruitmentArticle, Long> {
    List<LikeableRecruitmentArticle> findAllByFromMember(Member FromMember);

    Optional<LikeableRecruitmentArticle> findByRecruitmentArticleAndFromMember(RecruitmentArticle recruitmentArticle, Member member);
}