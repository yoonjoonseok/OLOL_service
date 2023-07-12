package com.ll.olol.boundedContext.notification.event;

import com.ll.olol.boundedContext.chat.entity.ChatMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterChatting extends ApplicationEvent {

    private final ChatMessage chatMessage;

    public EventAfterChatting(Object source, ChatMessage chatMessage) {
        super(source);
        this.chatMessage = chatMessage;
    }
}
