package com.ll.olol.boundedContext.notification.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationDTO {
    private String title;

    private String body;

    private String link;
}
