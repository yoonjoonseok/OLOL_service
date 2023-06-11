package com.ll.olol.boundedContext.report.repository;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.report.entity.ArticleReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ArticleReport, Long> {
    ArticleReport findByRecruitmentArticleAndFromMember(RecruitmentArticle recruitmentArticle, Member actor);

    ArticleReport findByRecruitmentArticle(RecruitmentArticle recruitmentArticle);
}
