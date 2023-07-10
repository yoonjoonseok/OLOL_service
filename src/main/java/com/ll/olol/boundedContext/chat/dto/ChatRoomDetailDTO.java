package com.ll.olol.boundedContext.chat.dto;

import com.ll.olol.boundedContext.chat.entity.ChatMessage;
import com.ll.olol.boundedContext.chat.entity.ChatRoom;
import com.ll.olol.boundedContext.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailDTO {

    private Long chatRoomId;
    private String roomName;
    private Member roomHost;
    private String roomId;
    private List<ChatMessage> chatMessageList;
    private String name;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public static ChatRoomDetailDTO toChatRoomDetailDTO(ChatRoom chatRoom) {
        ChatRoomDetailDTO chatRoomDetailDTO = new ChatRoomDetailDTO();
        List<ChatMessage> chatList = chatRoom.getChatList();

        chatRoomDetailDTO.setChatRoomId(chatRoom.getId());
        chatRoomDetailDTO.setRoomId(chatRoom.getRoomId());
        chatRoomDetailDTO.setRoomName(chatRoom.getRoomName());
        chatRoomDetailDTO.setRoomHost(chatRoom.getRoomHost());
        chatRoomDetailDTO.setChatMessageList(chatRoom.getChatList());

        if (chatList.size() == 0) {
            chatRoomDetailDTO.setLastMessage("입장해서 채팅하세요!");
            chatRoomDetailDTO.setLastMessageTime(LocalDateTime.now());
        } else {
            chatRoomDetailDTO.setLastMessage(chatList.get(chatList.size() - 1).getMessage());
            chatRoomDetailDTO.setLastMessageTime(chatList.get(chatList.size() - 1).getCreateDate());
        }

        return chatRoomDetailDTO;
    }

}