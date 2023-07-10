package com.ll.olol.boundedContext.notification.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.notification.entity.Notification;
import com.ll.olol.boundedContext.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/notification")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class NotificationController {
    private final Rq rq;
    private final NotificationService notificationService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String showList(Model model) {

        Member loginedMember = rq.getMember();
        if (!memberService.hasAdditionalInfo(loginedMember)) {
            return rq.historyBack("마이페이지에서 추가정보를 입력해주세요.");
        }

        List<Notification> notifications = notificationService.findByToInstaMember(rq.getMember());

        //notifications = notifications.stream().filter(n -> !n.isRead()).collect(Collectors.toList());

        Collections.reverse(notifications);

        notificationService.markAsRead(notifications);

        model.addAttribute("notifications", notifications);

        return "usr/notification/list";
    }

    @ResponseBody
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe() {
        Member member = rq.getMember();
        // 서비스를 통해 생성된 SseEmitter를 반환
        return notificationService.connectNotification(member.getId());
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody String token) {
        notificationService.register(rq.getMember().getId(), token);
        return ResponseEntity.ok().build();
    }
}