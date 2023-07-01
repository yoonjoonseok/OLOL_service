package com.ll.olol.boundedContext.chat.entity;

import com.ll.olol.base.entity.BaseEntity;
import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ChatMessage extends BaseEntity {

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public static ChatMessage create(String message, Member sender, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .message(message)
                .sender(sender)
                .chatRoom(chatRoom)
                .build();
    }
    
}