package com.ll.olol.boundedContext.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String showMain() {
        return "redirect:/recruitment/list";
    }

    @GetMapping("/adm")
    public String showAdmHome() {
        return "redirect:/member/login";
    }

    @GetMapping("/li")
    public String showlist() {
        return "usr/recruitment/list";
    }

    @GetMapping("/list")
    public String showAlllist() {
        return "usr/recruitment/allList";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String showHello() {
        return "hello";
    }
}
