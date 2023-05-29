package com.ll.olol.boundedContext.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String showMain(){
        return "usr/home/main";
    }

    @GetMapping("/hee")
    public String showHelloo(){
        return "usr/layout/hello";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String showHello(){
        return "hello";
    }
}
