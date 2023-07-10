package com.ll.olol.boundedContext.notification.eventListener;

import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.notification.entity.EmailMessage;
import com.ll.olol.boundedContext.notification.entity.Notification;
import com.ll.olol.boundedContext.notification.entity.NotificationDTO;
import com.ll.olol.boundedContext.notification.event.*;
import com.ll.olol.boundedContext.notification.service.EmailService;
import com.ll.olol.boundedContext.notification.service.FirebaseCloudMessageService;
import com.ll.olol.boundedContext.notification.service.NotificationService;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentPeopleService;
import com.ll.olol.boundedContext.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationEventListener {
    private final NotificationService notificationService;
    private final RecruitmentPeopleService recruitmentPeopleService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final EmailService emailService;
    private final ReviewService reviewService;

    @Value("${custom.site.baseUrl}")
    private String domain;

    private void sendNotifications(Notification notification, NotificationDTO notificationDTO) throws IOException {
        //notificationService.send(notification.getMember().getId(), notificationDTO); //sse
        firebaseCloudMessageService.sendMessageTo(notificationService.getTokenMap().get(notification.getMember().getId()), notificationDTO);

        if (notification.getType() >= 3 && notification.getType() <= 5) {
            EmailMessage emailMessage = EmailMessage.builder().to(notification.getMember().getEmail()).subject(notification.getContent()).message(notificationDTO.getLink()).build();
            emailService.sendMail(emailMessage);
        }
    }

    @EventListener
    public void listen(EventAfterComment event) throws IOException {
        RecruitmentArticle recruitmentArticle = event.getRecruitmentArticle();
        String content = recruitmentArticle.getArticleName() + " 공고에 댓글이 달렸습니다";
        Notification notification = notificationService.make(recruitmentArticle.getMember(), 1, content,
                recruitmentArticle.getId());

        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body(event.getComment().getContent()).link(domain + "recruitment/" + recruitmentArticle.getId()).build();

        sendNotifications(notification, notificationDTO);
    }

    @EventListener
    public void listen(EventAfterRecruitmentPeople event) throws IOException {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();
        Member member = recruitmentPeople.getRecruitmentArticle().getMember();
        String content =
                recruitmentPeople.getRecruitmentArticle().getArticleName() + " 공고에 " + recruitmentPeople.getMember()
                        .getNickname() + "님이 참가 신청을 하였습니다.";
        Notification notification = notificationService.make(member, 2, content, recruitmentPeople.getRecruitmentArticle().getId());

        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link(domain + "recruitment/fromList").build();

        sendNotifications(notification, notificationDTO);
    }

    @EventListener
    public void listen(EventAfterRecruitmentAttend event) throws IOException {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();
        Member member = recruitmentPeople.getMember();
        String content = recruitmentPeople.getRecruitmentArticle().getArticleName() + " 공고의 " + recruitmentPeople.getRecruitmentArticle().getMember().getNickname();
        if (recruitmentPeople.isAttend()) {
            content += "님이 참가 수락을 했습니다.";
        } else {
            content += "님이 참가 거절을 했습니다.";
        }

        Notification notification = notificationService.make(member, 3, content, recruitmentPeople.getRecruitmentArticle().getId());

        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link(domain + "member/mypage").build();

        sendNotifications(notification, notificationDTO);
    }

    @EventListener
    public void listen(EventAfterDeportPeople event) throws IOException {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();
        Member member = recruitmentPeople.getMember();
        String content =
                recruitmentPeople.getRecruitmentArticle().getArticleName() + " 공고의 " + recruitmentPeople.getRecruitmentArticle().getMember().getNickname() + "님께 "
                        + "추방되었습니다.";
        Notification notification = notificationService.make(member, 4, content, recruitmentPeople.getRecruitmentArticle().getId());

        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link(domain + "member/mypage").build();

        sendNotifications(notification, notificationDTO);
    }

    @EventListener
    public void listen(EventAfterUpdateArticle event) throws IOException {
        RecruitmentArticle recruitmentArticle = event.getRecruitmentArticle();
        List<RecruitmentPeople> list = recruitmentPeopleService.findByRecruitmentArticle(recruitmentArticle);
        for (RecruitmentPeople r : list) {
            String content = recruitmentArticle.getArticleName() + " 공고의 내용이 변경되었습니다";
            Notification notification = notificationService.make(r.getMember(), 5, content, recruitmentArticle.getId());

            NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link(domain + "recruitment/" + recruitmentArticle.getId()).build();

            sendNotifications(notification, notificationDTO);
        }
    }

    @EventListener
    public void listen(EventAfterCourseTime event) throws IOException {
        RecruitmentArticle recruitmentArticle = event.getRecruitmentArticle();
        Member author = recruitmentArticle.getMember();

        String content = recruitmentArticle.getArticleName() + " 공고의 산행이 완료 됐습니다.";

        RsData reviewMemberRsData = reviewService.createReviewMember(author, recruitmentArticle, null);

        reviewService.setCourseTimeEnd(recruitmentArticle);

        // 공고자에게 산행 종료 알림 보냄
        Notification notification = notificationService.makeReviewNotification(author, 6, content, recruitmentArticle.getId(), true);
        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link("").build();

        sendNotifications(notification, notificationDTO);
    }

    @EventListener
    public void listen(EventAfterCheckedRealParticipant event) throws IOException {
        RecruitmentPeople recruitmentPeople = event.getRecruitmentPeople();

        Member reviewer = recruitmentPeople.getMember();

        String content = recruitmentPeople.getRecruitmentArticle().getArticleName() + " 공고의 산행이 완료 됐습니다. 참여했던 인원들에게 후기를 남겨주세요.";

        // recruimentPeople 내부의 realParticipant 여부로 진짜 참여자를 판별
        RsData rsData = recruitmentPeopleService.checkedRealParticipant(recruitmentPeople, true);

        // recruimentPeople 내부의 realParticipant 여부로 진짜 참여자들을 리뷰 멤버에 추가
        RsData reviewMemberRsData = reviewService.createReviewMember(reviewer, recruitmentPeople.getRecruitmentArticle(), recruitmentPeople);

        Notification notification = notificationService.makeReviewWriteNotification(reviewer, 7, content, recruitmentPeople.getRecruitmentArticle().getId(), true);
        NotificationDTO notificationDTO = NotificationDTO.builder().title(notification.getContent()).body("").link("").build();

        sendNotifications(notification, notificationDTO);
    }
}
