package com.ll.olol.boundedContext.member.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.LikeableRecruitmentArticle;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.LikeableRecruitmentArticleService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class LikeableRecruitmentArticleController {
    private final Rq rq;
    private final LikeableRecruitmentArticleService likeableRecruitmentArticleService;
    private final RecruitmentService recruitmentService;

    @GetMapping("/bookmark")
    public String bookmark(Model model) {
        Member actor = rq.getMember();
        List<LikeableRecruitmentArticle> likeableRecruitmentArticles = likeableRecruitmentArticleService.findByFromMember(actor);

        model.addAttribute("likeableRecruitmentArticles", likeableRecruitmentArticles);
        return "usr/member/bookmark";
    }

    @PostMapping("/bookmark/{id}")
    public String add(@PathVariable Long id) {
        Optional<RecruitmentArticle> recruitmentArticle = recruitmentService.findById(id);

        if (recruitmentArticle.isEmpty()) {
            RsData rsData = RsData.of("F-1", "존재하지 않는 모임 공고입니다");
            return rq.historyBack(rsData);
        }

        Member actor = rq.getMember();
        Optional<LikeableRecruitmentArticle> likeableRecruitmentArticle = likeableRecruitmentArticleService.findByRecruitmentArticleAndFromMember(recruitmentArticle.get(), actor);

        if (likeableRecruitmentArticle.isPresent()) {
            RsData rsData = RsData.of("F-2", "이미 찜한 모임 공고입니다");
            return rq.historyBack(rsData);
        }

        LikeableRecruitmentArticle newLikeableRecruitmentArticle = LikeableRecruitmentArticle
                .builder()
                .recruitmentArticle(recruitmentArticle.get())
                .fromMember(actor)
                .build();

        likeableRecruitmentArticleService.add(newLikeableRecruitmentArticle);

        //return "redirect:/recruitment/" + id;
        return "redirect:/member/bookmark";
    }

    @DeleteMapping("/bookmark/{id}")
    public String cancel(@PathVariable Long id) {
        Member actor = rq.getMember();
        Optional<LikeableRecruitmentArticle> likeableRecruitmentArticle = likeableRecruitmentArticleService.findById(id);

        if (likeableRecruitmentArticle.isEmpty()) {
            RsData rsData = RsData.of("F-1", "존재하지 않는 찜입니다");
            return rq.historyBack(rsData);
        }

        if (actor.getId() != likeableRecruitmentArticle.get().getFromMember().getId()) {
            RsData rsData = RsData.of("F-2", "사용자가 일치하지 않습니다");
            return rq.historyBack(rsData);
        }

        likeableRecruitmentArticleService.delete(likeableRecruitmentArticle.get());

        return "redirect:/member/bookmark";
    }
}
