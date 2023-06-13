package com.ll.olol.boundedContext.notification.event;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterUpdateArticle extends ApplicationEvent {
    private final RecruitmentArticle recruitmentArticle;

    public EventAfterUpdateArticle(Object source, RecruitmentArticle recruitmentArticle) {
        super(source);
        this.recruitmentArticle = recruitmentArticle;
    }
}
