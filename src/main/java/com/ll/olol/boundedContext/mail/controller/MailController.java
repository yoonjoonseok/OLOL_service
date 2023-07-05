package com.ll.olol.boundedContext.mail.controller;

import com.ll.olol.boundedContext.mail.entity.MailDto;
import com.ll.olol.boundedContext.mail.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;

public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // 문의 답변 작성
    @PostMapping("/addAnswer.do")
    public String addAnswer(AnswerDto aDto, MailDto mailDto) {

        if (aDto.getStatus().equals("E")) {
            emailService.sendSimpleMessage(mailDto); // 이메일전송
        }

        return "board/questionDetail :: #answerList";
    }
}
