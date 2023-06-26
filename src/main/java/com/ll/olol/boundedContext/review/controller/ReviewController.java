package com.ll.olol.boundedContext.review.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final Rq rq;
    private final ReviewService reviewService;


    //    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {

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

    @PostMapping("/write")
    public String write(@Valid ReviewForm reviewForm) {
        Member me = rq.getMember();

        reviewService.write(reviewForm.reviewTypeCode, reviewForm.appointmentTimeCode, reviewForm.mannerCode, me);

        return "redirect:/";
    }

    @GetMapping("/list")
    public String showList() {

        return "usr/review/reviewWrite";
    }

}
