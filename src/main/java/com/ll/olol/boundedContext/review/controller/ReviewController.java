package com.ll.olol.boundedContext.review.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/review")
@RequiredArgsConstructor
public class ReviewController {
    private final Rq rq;
    private final ReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showLike() {
        return "usr/likeablePerson/like";
    }
}
