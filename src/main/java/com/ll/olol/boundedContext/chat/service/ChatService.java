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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @Transactional
    public ChatRoomDetailDTO createChatRoomDetailDTO(Member sender, Member receiver, Member roomHost) {

        ChatRoom chatRoom = createChatRoom(sender, receiver, roomHost);

        ChatRoomDetailDTO chatRoomDetailDTO = ChatRoomDetailDTO.toChatRoomDetailDTO(chatRoom);

        return chatRoomDetailDTO;
    }

    private ChatRoom createChatRoom(Member sender, Member receiver, Member roomHost) {
        ChatRoom chatRoom = ChatRoom.create(sender, receiver, roomHost);
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
        //필요한거 : message, Member sender, ChatRoom 객체
        //임시로 repository에 해놧음 service 구현되면 수정할 것
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElse(null);
        Member sender = memberService.findByNickname(writer).orElse(null);

        ChatMessage chatMessage = ChatMessage.create(message, sender, chatRoom);
        chatRoom.addMessage(chatMessage);
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findByRoomId(String roomId) {
        return chatMessageRepository.findByChatRoom_RoomId(roomId);
    }

    public List<ChatRoomDetailDTO> findByNickname(String nickname) {
        Member hostMember = memberService.findByNickname(nickname).get();
        long hostId = rq.getMember().getId();

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

    public ChatRoom findExistChatRoom(Long senderId, Long receiverId) {
        return chatRoomRepository.findExistChatRoom(senderId, receiverId).orElse(null);
    }

    public Boolean isExistChatRoom(Long senderId, Long receiverId) {
        return findExistChatRoom(senderId, receiverId) != null;
    }

}