package com.ll.olol.boundedContext.report.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import com.ll.olol.boundedContext.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/recruitment")
@RequiredArgsConstructor
public class ReportController {
    private final Rq rq;

    private final ReportService reportService;

    private final RecruitmentService recruitmentService;

//    @GetMapping("/report")
//    public String bookmark(Model model) {
//        Member actor = rq.getMember();
//        List<ArticleReport> reportRecruitmentArticles = reportService.findByFromMember(actor);
//
//        model.addAttribute("likeableRecruitmentArticles", likeableRecruitmentArticles);
//        return "usr/member/bookmark";
//    }

    @PostMapping("/report/{id}")
    public String add(@PathVariable Long id) {
        Optional<RecruitmentArticle> or = recruitmentService.findById(id);

        RecruitmentArticle recruitmentArticle = null;
        if (or.isPresent()) {
            recruitmentArticle = or.get();
        }
        Member actor = rq.getMember();

        RsData canAddRsData = reportService.canReport(recruitmentArticle, actor);

        if (canAddRsData.isFail())
            return rq.historyBack(canAddRsData);

        reportService.report(recruitmentArticle, actor);

        return "redirect:/recruitment/" + id;
        //return "redirect:/recruit/bookmark";
    }
}
