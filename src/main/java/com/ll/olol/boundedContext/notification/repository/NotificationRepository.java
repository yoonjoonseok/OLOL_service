package com.ll.olol.boundedContext.notification.repository;

import com.ll.olol.boundedContext.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
