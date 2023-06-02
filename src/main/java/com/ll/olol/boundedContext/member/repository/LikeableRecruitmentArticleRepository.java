package com.ll.olol.boundedContext.member.repository;

import com.ll.olol.boundedContext.member.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeableRecruitmentArticleRepository extends JpaRepository<LikeableRecruitmentArticle, Long> {
    List<LikeableRecruitmentArticle> findAllByFromMember(Member FromMember);
}