package com.ll.olol.boundedContext.notification.eventListener;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.notification.event.EventAfterComment;
import com.ll.olol.boundedContext.notification.event.EventAfterRecruitmentAttend;
import com.ll.olol.boundedContext.notification.event.EventAfterRecruitmentPeople;
import com.ll.olol.boundedContext.notification.service.NotificationService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
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

    @EventListener
    public void listen(EventAfterRecruitmentPeople event) {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();
        Member member = recruitmentPeople.getRecruitmentArticle().getMember();
        String content = recruitmentPeople.getId() + "에 참가 신청이 왔습니다.";
        notificationService.make(member, 1, content);
    }

    @EventListener
    public void listen(EventAfterRecruitmentAttend event) {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();
        Member member = recruitmentPeople.getMember();
        String content = "";
        if (recruitmentPeople.isAttend()) {
            content = recruitmentPeople.getRecruitmentArticle().getMember().getId() + "가 참가 수락을 했습니다."
                    + "링크는 " + recruitmentPeople.getRecruitmentArticle().getRecruitmentArticleForm().getConnectType();
        } else {
            content = recruitmentPeople.getRecruitmentArticle().getMember().getId() + "가 참가 거절을 했습니다.";
        }

        notificationService.make(member, 1, content);
    }


}
