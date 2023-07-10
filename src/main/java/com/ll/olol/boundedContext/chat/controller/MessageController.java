package com.ll.olol.boundedContext.chat.controller;

import com.ll.olol.boundedContext.chat.dto.ChatMessageDTO;
import com.ll.olol.boundedContext.chat.entity.ChatMessage;
import com.ll.olol.boundedContext.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDTO message) {
        String roomId = message.getRoomId();

        List<ChatMessage> messages = chatService.findMessagesByRoomId(roomId);

        if (messages.isEmpty()) {
            ChatMessageDTO enterMessage = ChatMessageDTO.create(roomId, message.getWriter(), message.getWriter() + "님이 입장하였습니다.");
            chatService.createChatMessage(enterMessage);
            template.convertAndSend("/sub/chat/room/" + roomId, enterMessage);
        }
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) {
        chatService.createChatMessage(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
