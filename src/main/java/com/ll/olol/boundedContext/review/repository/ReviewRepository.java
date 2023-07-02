package com.ll.olol.boundedContext.review.repository;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByRecruitmentArticleAndToMemberAndFromMember(RecruitmentArticle recruitmentArticle, Member reviewTarget, Member me);
}
