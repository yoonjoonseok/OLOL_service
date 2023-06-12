package com.ll.olol.boundedContext.notification.eventListener;

import com.ll.olol.boundedContext.notification.event.EventAfterComment;
import com.ll.olol.boundedContext.notification.service.NotificationService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationEventListener {
    private final NotificationService notificationService;

    @EventListener
    public void listen(EventAfterComment event) {
        RecruitmentArticle recruitmentArticle = event.getRecruitmentArticle();
        String content = recruitmentArticle.getArticleName() + "에 댓글이 달렸습니다";
        notificationService.make(recruitmentArticle.getMember(), 1, content);
    }
}
