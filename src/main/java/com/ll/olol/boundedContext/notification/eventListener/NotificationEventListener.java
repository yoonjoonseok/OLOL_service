package com.ll.olol.boundedContext.notification.eventListener;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.notification.entity.Notification;
import com.ll.olol.boundedContext.notification.entity.NotificationDTO;
import com.ll.olol.boundedContext.notification.event.*;
import com.ll.olol.boundedContext.notification.service.NotificationService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationEventListener {
    private final NotificationService notificationService;
    private final RecruitmentPeopleService recruitmentPeopleService;
    @Value("${custom.site.baseUrl}")
    private String domain;

    @EventListener
    public void listen(EventAfterComment event) {
        RecruitmentArticle recruitmentArticle = event.getRecruitmentArticle();
        String content = recruitmentArticle.getArticleName() + " 공고에 댓글이 달렸습니다";
        Notification notification = notificationService.make(recruitmentArticle.getMember(), 1, content,
                recruitmentArticle.getId());

        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body(event.getComment().getContent()).link(domain + "recruitment/" + recruitmentArticle.getId()).build();
        notificationService.send(notification.getMember().getId(), notificationDTO);
    }

    @EventListener
    public void listen(EventAfterRecruitmentPeople event) {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();
        Member member = recruitmentPeople.getRecruitmentArticle().getMember();
        String content =
                recruitmentPeople.getRecruitmentArticle().getArticleName() + " 공고에 " + recruitmentPeople.getMember()
                        .getNickname() + "님이 참가 신청을 하였습니다.";
        Notification notification = notificationService.make(member, 2, content, recruitmentPeople.getRecruitmentArticle().getId());

        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link(domain + "recruitment/fromList").build();
        notificationService.send(notification.getMember().getId(), notificationDTO);
    }

    @EventListener
    public void listen(EventAfterRecruitmentAttend event) {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();
        Member member = recruitmentPeople.getMember();
        String content = recruitmentPeople.getRecruitmentArticle().getArticleName() + " 공고의 " + recruitmentPeople.getRecruitmentArticle().getMember().getNickname();
        if (recruitmentPeople.isAttend()) {
            content += "님이 참가 수락을 했습니다.\n"
                    + "링크: " + recruitmentPeople.getRecruitmentArticle().getRecruitmentArticleForm().getConnectType();
        } else {
            content += "님이 참가 거절을 했습니다.";
        }

        Notification notification = notificationService.make(member, 3, content, recruitmentPeople.getRecruitmentArticle().getId());

        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link(domain + "member/mypage").build();
        notificationService.send(notification.getMember().getId(), notificationDTO);
    }

    @EventListener
    public void listen(EventAfterDeportPeople event) {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();
        Member member = recruitmentPeople.getMember();
        String content =
                recruitmentPeople.getRecruitmentArticle().getArticleName() + " 공고의 " + recruitmentPeople.getRecruitmentArticle().getMember().getNickname() + "님이 "
                        + "추방을 했습니다.";
        Notification notification = notificationService.make(member, 4, content, recruitmentPeople.getRecruitmentArticle().getId());

        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link(domain + "member/mypage").build();
        notificationService.send(notification.getMember().getId(), notificationDTO);
    }

    @EventListener
    public void listen(EventAfterUpdateArticle event) {
        RecruitmentArticle recruitmentArticle = event.getRecruitmentArticle();
        List<RecruitmentPeople> list = recruitmentPeopleService.findByRecruitmentArticle(recruitmentArticle);
        for (RecruitmentPeople r : list) {
            String content = recruitmentArticle.getArticleName() + " 공고의 내용이 변경되었습니다";
            Notification notification = notificationService.make(r.getMember(), 5, content, recruitmentArticle.getId());

            NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link(domain + "recruitment/" + recruitmentArticle.getId()).build();
            notificationService.send(notification.getMember().getId(), notificationDTO);
        }
    }
}
