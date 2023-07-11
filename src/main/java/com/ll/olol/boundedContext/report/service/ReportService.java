package com.ll.olol.boundedContext.report.service;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.report.entity.ArticleReport;
import com.ll.olol.boundedContext.report.repository.ReportRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class ReportService {
    private final ReportRepository reportRepository;

    @Transactional
    public RsData report(RecruitmentArticle recruitmentArticle, Member actor, int reason) {

        ArticleReport articleReport = ArticleReport
                .builder()
                .recruitmentArticle(recruitmentArticle)
                .fromMember(actor)
                .reason(reason)
                .build();

        reportRepository.save(articleReport);
        return RsData.of("S-1", "신고 완료");
    }

    public RsData canReport(RecruitmentArticle recruitmentArticle, Member actor) {
        if (recruitmentArticle == null) {
            return RsData.of("F-1", "존재하지 않는 모임 공고입니다");
        }

        ArticleReport articleReport = reportRepository.findByRecruitmentArticleAndFromMember(recruitmentArticle, actor);

        if (articleReport != null) {
            return RsData.of("F-2", "이미 신고한 모임 공고입니다");
        }

        return RsData.of("S-1", "신고 가능한 모임 공고입니다.");
    }

    public List<ArticleReport> findAll() {
        return reportRepository.findAll();
    }

    public boolean isExistReportArticle(RecruitmentArticle recruitmentArticle) {
        ArticleReport articleReport = reportRepository.findByRecruitmentArticle(recruitmentArticle);
        return articleReport != null;
    }


    private Specification<ArticleReport> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ArticleReport> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<ArticleReport, Member> u1 = q.join("member", JoinType.LEFT);
                Join<ArticleReport, RecruitmentArticle> a = q.join("recruitmentArticle", JoinType.LEFT);
                Join<RecruitmentArticle, Member> u2 = a.join("member", JoinType.LEFT);
                return cb.or(cb.like(a.get("articleName"), "%" + kw + "%"), // 제목
                        cb.like(a.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(a.get("recruitmentArticleForm").get("mountainName"), "%" + kw + "%"),
                        cb.like(u1.get("nickname"), "%" + kw + "%"),
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("nickname"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public Page<ArticleReport> getlist(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        if (kw == null || kw.trim().length() == 0) {
            return reportRepository.findAll(pageable);
        }
        Specification<ArticleReport> spec = search(kw);
        return reportRepository.findAll(spec, pageable);
    }

    public Page<ArticleReport> getListByConditions(int reason, String kw, Pageable pageable) {
        Specification<ArticleReport> spec = Specification.where(null);

        if (reason != 0) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("reason"), reason));
        }

        if (kw != null && !kw.trim().isEmpty()) {
            spec = spec.and(search(kw));
        }

        return reportRepository.findAll(spec, pageable);
    }

    public int countReport(Member member) {
        List<RecruitmentArticle> recruitmentArticle = member.getRecruitmentArticle();
        int count = 0;
        for (RecruitmentArticle data : recruitmentArticle) {
            ArticleReport byRecruitmentArticle = reportRepository.findByRecruitmentArticle(data);
            if (byRecruitmentArticle != null) {
                count++;
            }
        }
        return count;
    }
}
