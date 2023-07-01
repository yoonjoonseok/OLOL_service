package com.ll.olol.boundedContext.chat.entity;

import com.ll.olol.base.entity.BaseEntity;
import com.ll.olol.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ChatRoom extends BaseEntity {

    private String roomId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ChatMessage> chatList = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom", cascade = CascadeType.ALL)
//    @Builder.Default
//    private List<ChatMember> chatMembers = new ArrayList<>();

    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "room_host")
    private Member roomHost;


    public static ChatRoom create(Member sender, Member receiver, Member roomHost) {
        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .sender(sender)
                .receiver(receiver)
                .chatList(new ArrayList<>())
                .roomHost(roomHost)
                .build();
    }

    public void addMessage(ChatMessage chatMessage) {
        chatList.add(chatMessage);

//        chatMessage.getChatRoom().addMessage(chatMessage);
    }
}