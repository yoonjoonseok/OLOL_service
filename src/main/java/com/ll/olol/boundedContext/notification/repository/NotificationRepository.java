package com.ll.olol.boundedContext.notification.repository;

import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    int countByMemberAndReadDateIsNull(Member member);

    List<Notification> findByMemberOrderByIdDesc(Member member);

    void deleteByMember(Member member);
}
