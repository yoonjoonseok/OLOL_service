package com.ll.olol.boundedContext.review.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import com.ll.olol.boundedContext.review.entity.Review;
import com.ll.olol.boundedContext.review.entity.ReviewMember;
import com.ll.olol.boundedContext.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final Rq rq;

    private final RecruitmentService recruitmentService;

    private final RecruitmentPeopleService recruitmentPeopleService;
    private final ReviewService reviewService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write/{id}")
    public String showWrite(@PathVariable Long id, Model model) {

        ReviewMember reviewMember = reviewService.reviewMemberFindById(id);


        Member reviewTarget = reviewMember.getReviewMember();

        Member me = rq.getMember();

        Review review = reviewService.findReviewWrite(reviewTarget, me, reviewMember.getRecruitmentArticle());


        if (review != null) {
            return rq.historyBack("이미 작성하셨습니다.");
        }


        return "usr/review/reviewWrite";
    }


    @AllArgsConstructor
    @Getter
    public static class ReviewForm {
        @NotNull
        @Min(1)
        @Max(3)
        private final int reviewTypeCode;

        @NotNull
        @Min(1)
        @Max(3)
        private final int appointmentTimeCode;

        @NotNull
        @Min(1)
        @Max(3)
        private final int mannerCode;

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write/{id}")
    public String write(@Valid ReviewForm reviewForm, @PathVariable Long id, Model model, HttpServletRequest request) {
        ReviewMember reviewMember = reviewService.reviewMemberFindById(id);
        Long articleId = reviewMember.getRecruitmentArticle().getId();

        Member reviewTarget = reviewMember.getReviewMember();

        Member me = rq.getMember();

        reviewService.write(reviewForm.reviewTypeCode, reviewForm.appointmentTimeCode, reviewForm.mannerCode, me, reviewTarget, reviewMember.getRecruitmentArticle());

//        RsData<Review> canReviewWrite = reviewService.canReviewWrite(reviewTarget, me, reviewMember.getRecruitmentArticle());
        Review review = reviewService.findReviewWrite(reviewTarget, me, reviewMember.getRecruitmentArticle());

//        List<Review> wroteReviewList = new ArrayList<>();
//
        HttpSession session = request.getSession();

//        if (canReviewWrite.isFail()) {
//            session.setAttribute("ReviewData", canReviewWrite.getData());
//        }
        session.setAttribute("ReviewData", review);


        return "redirect:/review/participantList/" + articleId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/reviewerList/{id}")
    public String showReviewerList(@PathVariable Long id, Model model) {
        Member author = rq.getMember();


        RecruitmentArticle recruitmentArticle = recruitmentService.findById(id).get();

        if (!recruitmentArticle.isCourseTimeEnd()) {
            return rq.historyBack("아직 리뷰 작성시간이 아닙니다.");
        }

        List<RecruitmentPeople> recruitmentPeopleList = recruitmentArticle.getRecruitmentPeople();
        List<RecruitmentPeople> attendPeopleList = recruitmentPeopleList
                .stream()
                .filter(e -> e.isAttend())
                .collect(Collectors.toList());

        model.addAttribute("recruitmentArticle", recruitmentArticle);
        model.addAttribute("attendList", attendPeopleList);

        return "usr/review/authorReviewerCheckList";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/realMember/{id}")
    public String checkedRealParticipant(@PathVariable Long id, Model model) {
        RecruitmentPeople recruitmentPeople = recruitmentPeopleService.findById(id);
        Long articleId = recruitmentPeople.getRecruitmentArticle().getId();

        if (!recruitmentPeople.getRecruitmentArticle().isCourseTimeEnd()) {
            return rq.historyBack("아직 리뷰 작성시간이 아닙니다.");
        }

        RsData rsData = reviewService.realParticipant(recruitmentPeople);


        rq.historyBack("성공띠");


        return "redirect:/review/reviewerList/" + articleId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/participantList/{id}")
    public String showRealParticipantList(@PathVariable Long id, Model model, HttpServletRequest request) {
        Member reviewer = rq.getMember();

        RecruitmentArticle recruitmentArticle = recruitmentService.findById(id).get();

        RsData checkReviewRsData = reviewService.findReviewMember(recruitmentArticle, reviewer);

//        RsData<RecruitmentPeople> checkRealParticipant = recruitmentPeopleService.findByRecruitmentArticleAndMember(recruitmentArticle, reviewer);

        if (checkReviewRsData.isFail()) {
            return rq.historyBack("후기를 작성할 권한이 없습니다.");
        }


        Member author = recruitmentArticle.getMember();

//        List<Member> realParticipantMemberList = reviewService.findRealParticipant(reviewer, author, recruitmentArticle);
        List<RecruitmentPeople> realParticipantList = reviewService.findRealParticipant(reviewer, author, recruitmentArticle);

        List<ReviewMember> reviewMemberList = reviewService.findAllByRecruitmentArticle(recruitmentArticle);

        model.addAttribute("recruitmentArticle", recruitmentArticle);
        model.addAttribute("realParticipantList", realParticipantList);
//        model.addAttribute("realParticipantMemberList", realParticipantMemberList);
        model.addAttribute("me", reviewer);

        model.addAttribute("reviewMemberList", reviewMemberList);

//        List<Review> wroteReviewList = new ArrayList<>();
//
//        HttpSession session = request.getSession();
//
//        Review reviewIWroteToWho = (Review) session.getAttribute("ReviewData");
//
//        if (reviewIWroteToWho != null) {
//            wroteReviewList.add(reviewIWroteToWho);
//        }

//        model.addAttribute("wroteReviewList", wroteReviewList);

        return "/usr/review/realParticipantMemberList";
    }


}
