package com.ll.olol.boundedContext.mail.service;

import com.ll.olol.boundedContext.mail.entity.MailDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("behappy0916@naver.com");
        message.setTo(mailDto.getEmailAddr());
        message.setSubject("[문의사항 답변입니다] " + mailDto.getEmailTitle());
        message.setText("[문의사항 답변입니다] " + mailDto.getEmailContent());
        emailSender.send(message);
    }
}