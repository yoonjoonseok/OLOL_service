package com.ll.olol.boundedContext.notification.event;

import com.ll.olol.boundedContext.comment.entity.Comment;
import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterComment extends ApplicationEvent {
    private final RecruitmentArticle recruitmentArticle;

    private final Comment comment;

    public EventAfterComment(Object source, RecruitmentArticle recruitmentArticle, Comment comment) {
        super(source);
        this.recruitmentArticle = recruitmentArticle;
        this.comment = comment;
    }

}