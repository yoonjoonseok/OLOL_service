package com.ll.olol.boundedContext.recruitment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recruitment")
public class RecruitmentController {
    @GetMapping("/{id}")
    public String showDetail(){
        return "usr/recruitment/detail";
    }
}
