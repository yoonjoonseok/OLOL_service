package com.ll.olol.boundedContext.notification.event;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentPeople;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterRecruitmentPeople extends ApplicationEvent {
    private final RecruitmentPeople recruitmentPeople;

    public EventAfterRecruitmentPeople(Object source, RecruitmentPeople recruitmentPeople) {
        super(source);
        this.recruitmentPeople = recruitmentPeople;
    }
}