package com.ll.olol.boundedContext.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.olol.base.rsData.RsData;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.notification.entity.Notification;
import com.ll.olol.boundedContext.notification.entity.NotificationDTO;
import com.ll.olol.boundedContext.notification.repository.EmitterRepository;
import com.ll.olol.boundedContext.notification.repository.NotificationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    private final static Long DEFAULT_TIMEOUT = 3600000L;
    private final static String NOTIFICATION_NAME = "notify";
    @Getter
    private final Map<Long, String> tokenMap = new HashMap<>();

    public List<Notification> findByMemberOrderByIdDesc(Member member) {
        return notificationRepository.findByMemberOrderByIdDesc(member);
    }

    @Transactional
    public Notification make(Member member, int type, String content, Long articleId, String link) {
        Notification notification = Notification.builder()
                .member(member)
                .type(type)
                .content(content)
                .articleId(articleId)
                .link(link)
                .build();

        notificationRepository.save(notification);

        return notification;
    }

    @Transactional
    public Notification makeReviewNotification(Member member, int type, String content, Long articleId, boolean reviewed, String link) {
        Notification notification = Notification.builder()
                .member(member)
                .type(type)
                .content(content)
                .articleId(articleId)
                .reviewed(reviewed)
                .link(link)
                .build();

        notificationRepository.save(notification);

        return notification;
    }

    @Transactional
    public Notification makeReviewWriteNotification(Member reviewer, int type, String content, Long articleId, boolean participant, String link) {
        Notification notification = Notification.builder()
                .member(reviewer)
                .type(type)
                .content(content)
                .articleId(articleId)
                .participant(participant)
                .link(link)
                .build();

        notificationRepository.save(notification);

        return notification;
    }

    @Transactional
    public RsData deleteByMember(Member member) {
        System.out.println("아" + member.getId());
        notificationRepository.deleteByMember(member);
        return RsData.of("S-1", "알림이 전부 삭제되었습니다.");
    }

    @Transactional
    public RsData markAsRead(List<Notification> notifications) {
        notifications
                .stream()
                .filter(notification -> !notification.isRead())
                .forEach(Notification::markAsRead);

        return RsData.of("S-1", "읽음 처리 되었습니다.");
    }

    public boolean countUnreadNotificationsByMember(Member member) {
        return notificationRepository.countByMemberAndReadDateIsNull(member) > 0;
    }

    public SseEmitter connectNotification(Long userId) {
        // 새로운 SseEmitter를 만든다
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        // 유저 ID로 SseEmitter를 저장한다.
        emitterRepository.save(userId, sseEmitter);

        // 세션이 종료될 경우 저장한 SseEmitter를 삭제한다.
        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        // 503 Service Unavailable 오류가 발생하지 않도록 첫 데이터를 보낸다.
        try {
            sseEmitter.send(SseEmitter.event().id("").name(NOTIFICATION_NAME).data("Connection completed"));
        } catch (IOException exception) {
            //throw new ApplicationException(ErrorCode.NOTIFICATION_CONNECTION_ERROR);
        }
        return sseEmitter;
    }

    public void send(Long userId, NotificationDTO notificationDTO) {
        String json = null;
        try {
            // 객체를 JSON 문자열로 변환
            json = mapper.writeValueAsString(notificationDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 유저 ID로 SseEmitter를 찾아 이벤트를 발생 시킨다.
        String finalJson = json;
        emitterRepository.get(userId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(finalJson);
            } catch (IOException exception) {
                // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
                emitterRepository.delete(userId);
                //throw new ApplicationException(ErrorCode.NOTIFICATION_CONNECTION_ERROR);
            }
        }, () -> log.info("No emitter found"));
    }

    public void register(final Long userId, final String token) {
        tokenMap.put(userId, token);
    }
}
