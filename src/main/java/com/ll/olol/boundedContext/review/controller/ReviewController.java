package com.ll.olol.boundedContext.review.controller;

import com.ll.olol.base.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/review")
@RequiredArgsConstructor
public class ReviewController {
    private final Rq rq;

    
}
