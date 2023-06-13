package com.ll.olol.boundedContext.report.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import com.ll.olol.boundedContext.report.entity.ArticleReport;
import com.ll.olol.boundedContext.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/report/{id}")
    public String add(@PathVariable Long id, @RequestParam int reason) {
        Optional<RecruitmentArticle> or = recruitmentService.findById(id);

        RecruitmentArticle recruitmentArticle = null;
        if (or.isPresent()) {
            recruitmentArticle = or.get();
        }
        Member actor = rq.getMember();

        RsData canAddRsData = reportService.canReport(recruitmentArticle, actor);

        if (canAddRsData.isFail())
            return rq.historyBack(canAddRsData);


        reportService.report(recruitmentArticle, actor, reason);

        return "redirect:/recruitment/" + id;
        //return "redirect:/recruit/bookmark";
    }

//    @PreAuthorize("hasAuthority('admin')")
//    @GetMapping("/report/list")
//    public String allReport(Model model) {
//        List<ArticleReport> articleReportList = reportService.findAll();
//        model.addAttribute(articleReportList);
//
//        return "adm/reportRecruitment/reportArticlelist";
//    }


    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/report/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int reason,
                       @RequestParam(defaultValue = "0") int page,
                       String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));


        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        Page<ArticleReport> paging = reportService.getListByConditions(reason, kw, pageable);

        model.addAttribute("paging", paging);
        return "adm/reportRecruitment/reportArticlelist";
    }

}
