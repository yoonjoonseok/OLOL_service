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

    private String roomName;

    @Column(unique = true)
    private String roomId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ChatMessage> chatList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "room_host_id")
    private Member roomHost;

    @ManyToMany
    @JoinTable(name = "chat_room_member",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> chatMembers = new ArrayList<>();


    public static ChatRoom create(Member roomHost, String roomName) {

        return ChatRoom.builder()
                .roomName(roomName)
                .roomId(UUID.randomUUID().toString())
                .roomHost(roomHost)
                .chatList(new ArrayList<>())
                .chatMembers(new ArrayList<>())
                .build();
    }

    public void addMessage(ChatMessage chatMessage) {
        chatList.add(chatMessage);
    }
}
