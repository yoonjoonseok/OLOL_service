package com.ll.olol.boundedContext.report.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.report.entity.ArticleReport;
import com.ll.olol.boundedContext.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class ReportService {
    private final ReportRepository reportRepository;

//    public List<ArticleReport> findByFromMember(Member actor) {
//    }

    @Transactional
    public void report(RecruitmentArticle recruitmentArticle, Member actor) {
        ArticleReport articleReport = ArticleReport
                .builder()
                .recruitmentArticle(recruitmentArticle)
                .fromMember(actor)
                .build();

        reportRepository.save(articleReport);
    }

    public RsData canReport(RecruitmentArticle recruitmentArticle, Member actor) {
        if (recruitmentArticle == null)
            return RsData.of("F-1", "존재하지 않는 모임 공고입니다");

        ArticleReport articleReport = reportRepository.findByRecruitmentArticleAndFromMember(recruitmentArticle, actor);

        if (articleReport != null)
            return RsData.of("F-2", "이미 신고한 모임 공고입니다");

        return RsData.of("S-1", "신고 가능한 모임 공고입니다");
    }
}
