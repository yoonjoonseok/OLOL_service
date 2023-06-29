package com.ll.olol.boundedContext.review.repository;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.review.entity.ReviewMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewMemberRepository extends JpaRepository<ReviewMember, Long> {
    List<ReviewMember> findAllByRecruitmentArticle(RecruitmentArticle recruitmentArticle);
}