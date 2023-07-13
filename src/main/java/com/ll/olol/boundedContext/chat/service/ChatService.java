package com.ll.olol.boundedContext.chat.service;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.chat.dto.ChatMessageDTO;
import com.ll.olol.boundedContext.chat.dto.ChatRoomDetailDTO;
import com.ll.olol.boundedContext.chat.entity.ChatMessage;
import com.ll.olol.boundedContext.chat.entity.ChatRoom;
import com.ll.olol.boundedContext.chat.repository.ChatMessageRepository;
import com.ll.olol.boundedContext.chat.repository.ChatRoomRepository;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.MemberService;
import com.ll.olol.boundedContext.notification.event.EventAfterChatting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatService {

    private final Rq rq;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public ChatRoomDetailDTO createChatRoomDetailDTO(Member roomHost, String roomName) {
        ChatRoom chatRoom = createChatRoom(roomHost, roomName);
        ChatRoomDetailDTO chatRoomDetailDTO = ChatRoomDetailDTO.toChatRoomDetailDTO(chatRoom);
        return chatRoomDetailDTO;
    }

    private ChatRoom createChatRoom(Member roomHost, String roomName) {
        ChatRoom chatRoom = ChatRoom.create(roomHost, roomName);
        chatRoom.getChatMembers().add(roomHost);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public ChatRoomDetailDTO findRoomById(String id) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(id).orElse(null);
        return ChatRoomDetailDTO.toChatRoomDetailDTO(chatRoom);
    }


    @Transactional
    public void createChatMessage(ChatMessageDTO chatMessageDTO) {
        String message = chatMessageDTO.getMessage();
        String roomId = chatMessageDTO.getRoomId();
        String writer = chatMessageDTO.getWriter();

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElse(null);
        Member sender = memberService.findByNickname(writer).orElse(null);

        if (chatRoom != null && sender != null) {
            ChatMessage chatMessage = ChatMessage.create(message, sender, chatRoom);
            chatRoom.addMessage(chatMessage);
            chatMessageRepository.save(chatMessage);

            publisher.publishEvent(new EventAfterChatting(this, chatMessage));
        }
    }

    public ChatRoom findByRoomName(String roomName) {
        return chatRoomRepository.findByRoomName(roomName).orElse(null);
    }


    public List<ChatMessage> findMessagesByRoomId(String roomId) {
        return chatMessageRepository.findByChatRoom_RoomId(roomId);
    }


    public List<ChatRoomDetailDTO> findByNickname(String nickname) {
        Member hostMember = memberService.findByNickname(nickname).get();
        long hostId = hostMember.getId();

        List<ChatRoom> chatRooms = chatRoomRepository.findByRoomHostId(hostId);
        List<ChatRoomDetailDTO> chatRoomDetailDTOS = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDetailDTOS.add(ChatRoomDetailDTO.toChatRoomDetailDTO(chatRoom));
        }

        return chatRoomDetailDTOS;
    }

    public List<ChatRoomDetailDTO> findByUsername(String username) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByChatRoom_Username(username);

        List<ChatRoomDetailDTO> chatRoomDetailDTOS = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDetailDTOS.add(ChatRoomDetailDTO.toChatRoomDetailDTO(chatRoom));
        }

        return chatRoomDetailDTOS;
    }

    public List<ChatRoomDetailDTO> findChatRoomsByMemberId(Long id) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByMemberId(id);
        List<ChatRoomDetailDTO> chatRoomDetailDTOS = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDetailDTOS.add(ChatRoomDetailDTO.toChatRoomDetailDTO(chatRoom));
        }

        return chatRoomDetailDTOS;
    }

}
