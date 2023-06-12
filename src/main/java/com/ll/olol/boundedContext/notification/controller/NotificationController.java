package com.ll.olol.boundedContext.notification.controller;


import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final Rq rq;
    private final NotificationService notificationService;

//    @GetMapping("/list")
//    @PreAuthorize("isAuthenticated()")
//    public String showList(Model model){
//
//        List
//    }
}
