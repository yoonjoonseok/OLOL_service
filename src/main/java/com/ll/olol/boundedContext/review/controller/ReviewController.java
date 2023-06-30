package com.ll.olol.boundedContext.review.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import com.ll.olol.boundedContext.review.entity.ReviewMember;
import com.ll.olol.boundedContext.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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


    //    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write/{id}")
    public String showWrite(@PathVariable Long id, Model model) {

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

    @PostMapping("/write/{id}")
    public String write(@Valid ReviewForm reviewForm, @PathVariable Long id, Model model) {
        ReviewMember reviewMember = reviewService.reviewMemberFindById(id);
        Long articleId = reviewMember.getRecruitmentArticle().getId();

        Member reviewTarget = reviewMember.getReviewMember();

        Member me = rq.getMember();

        reviewService.write(reviewForm.reviewTypeCode, reviewForm.appointmentTimeCode, reviewForm.mannerCode, me, reviewTarget);

        reviewService.updateReviewComplete(reviewMember, true);

        return "redirect:/review/participantList/" + articleId;
    }

    @GetMapping("/reviewerList/{id}")
    public String showReviewerList(@PathVariable Long id, Model model) {
        Member author = rq.getMember();

        RecruitmentArticle recruitmentArticle = recruitmentService.findById(id).get();

        List<RecruitmentPeople> recruitmentPeopleList = recruitmentArticle.getRecruitmentPeople();
        List<RecruitmentPeople> attendPeopleList = recruitmentPeopleList
                .stream()
                .filter(e -> e.isAttend())
                .collect(Collectors.toList());

        model.addAttribute("recruitmentArticle", recruitmentArticle);
        model.addAttribute("attendList", attendPeopleList);

        return "usr/review/authorReviewerCheckList";
    }


//    @PostMapping("/publish-event-endpoint")
//    public void publishEvent(@RequestBody RecruitmentPeople recruitmentPeople) {
//        reviewService.realParticipant(recruitmentPeople);
//        // eventData를 기반으로 이벤트를 발행하거나 처리하는 로직을 구현
////        publisher.publishEvent(new EventAfterUpdateArticle(this, eventData.getRecruitmentPeople()));
//    }

    @GetMapping("/realMember/{id}")
    public String checkedRealParticipant(@PathVariable Long id, Model model) {
        RecruitmentPeople recruitmentPeople = recruitmentPeopleService.findById(id);
        Long articleId = recruitmentPeople.getRecruitmentArticle().getId();

        RsData rsData = reviewService.realParticipant(recruitmentPeople);


        rq.historyBack("성공띠");


//        return "usr/review/authorReviewerCheckList";
        return "redirect:/review/reviewerList/" + articleId;
    }

    @GetMapping("/participantList/{id}")
    public String showRealParticipantList(@PathVariable Long id, Model model) {
        Member reviewer = rq.getMember();

        RecruitmentArticle recruitmentArticle = recruitmentService.findById(id).get();

        Member author = recruitmentArticle.getMember();

//        List<Member> realParticipantMemberList = reviewService.findRealParticipant(reviewer, author, recruitmentArticle);
        List<RecruitmentPeople> realParticipantList = reviewService.findRealParticipant(reviewer, author, recruitmentArticle);

        List<ReviewMember> reviewMemberList = reviewService.findAllByRecruitmentArticle(recruitmentArticle);

        model.addAttribute("recruitmentArticle", recruitmentArticle);
        model.addAttribute("realParticipantList", realParticipantList);
//        model.addAttribute("realParticipantMemberList", realParticipantMemberList);
        model.addAttribute("me", reviewer);

        model.addAttribute("reviewMemberList", reviewMemberList);

//        return "usr/review/authorReviewerCheckList";
        return "/usr/review/realParticipantMemberList";
    }


    @GetMapping("/list")
    public String showList() {

        return "usr/review/reviewWrite";
    }

}
