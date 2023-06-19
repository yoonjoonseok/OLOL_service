package com.ll.olol.boundedContext.recruitment.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.service.LikeableRecruitmentArticleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class LikeableRecruitmentArticleController {
    private final Rq rq;
    private final LikeableRecruitmentArticleService likeableRecruitmentArticleService;
    private final RecruitmentService recruitmentService;

    @GetMapping("/bookmark")
    public String bookmark(Model model) {
        Member actor = rq.getMember();
        List<LikeableRecruitmentArticle> likeableRecruitmentArticles = likeableRecruitmentArticleService.findByFromMember(actor);

        Collections.reverse(likeableRecruitmentArticles);

        model.addAttribute("likeableRecruitmentArticles", likeableRecruitmentArticles);
        return "usr/member/bookmark";
    }

    @PostMapping("/bookmark/{id}")
    public String add(@PathVariable Long id) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);
        Member actor = rq.getMember();

        RsData canAddRsData = likeableRecruitmentArticleService.canAdd(recruitmentArticle, actor);

        if (canAddRsData.isFail())
            return rq.historyBack(canAddRsData);

        likeableRecruitmentArticleService.add(recruitmentArticle.get(), actor);

        //return "redirect:/recruitment/" + id;
        return "redirect:/member/mypage";
    }

    @DeleteMapping("/bookmark/{id}")
    public String cancel(@PathVariable Long id) {
        Optional<LikeableRecruitmentArticle> likeableRecruitmentArticle = likeableRecruitmentArticleService.findById(id);

        RsData canCancelRsData = likeableRecruitmentArticleService.canCancel(likeableRecruitmentArticle, rq.getMember());

        if (canCancelRsData.isFail())
            return rq.historyBack(canCancelRsData);

        likeableRecruitmentArticleService.cancel(likeableRecruitmentArticle.get());

        return "redirect:/member/mypage";
    }
}
