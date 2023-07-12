package com.ll.olol.boundedContext.chat.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.chat.dto.ChatRoomDetailDTO;
import com.ll.olol.boundedContext.chat.entity.ChatMessage;
import com.ll.olol.boundedContext.chat.entity.ChatRoom;
import com.ll.olol.boundedContext.chat.repository.ChatRoomRepository;
import com.ll.olol.boundedContext.chat.service.ChatService;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final MessageController messageController;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    private final Rq rq;

    //나의 채팅방 목록 조회
    @GetMapping("/rooms")
    @PreAuthorize("isAuthenticated()")
    public String myRooms(@AuthenticationPrincipal User user, Model model) {
        Member loginedMember = memberService.findByUsername(user.getUsername()).orElse(null);

        long memberId = loginedMember.getId();
        String nickname = loginedMember.getNickname();
        List<ChatRoomDetailDTO> roomList = chatService.findChatRoomsByMemberId(memberId);

        model.addAttribute("myNickname", nickname);
        model.addAttribute("roomList", roomList);

        return "usr/chat/chatList";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createRoom(@AuthenticationPrincipal User user, String roomName) {
        chatService.createChatRoomDetailDTO(rq.getMember(), roomName);

        return rq.redirectWithMsg("/chat/rooms", "채팅방이 개설되었습니다.");
    }

    //채팅방 상세
    @GetMapping("/room/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public String getRoom(@PathVariable String roomId, Model model, @AuthenticationPrincipal User user) {

        List<ChatMessage> messages = chatService.findMessagesByRoomId(roomId);
        Member member = memberService.findByUsername(user.getUsername()).orElse(null);


        if (member == null) {
            return rq.redirectWithMsg("/user/member/login", "다시 로그인해주세요!");
        }

        ChatRoom toJoinRoom = chatRoomRepository.findByRoomId(roomId).orElse(null);
        if (toJoinRoom != null) {
            List<Member> roomMembers = toJoinRoom.getChatMembers();
            if (!roomMembers.contains(member)) {
                roomMembers.add(member);
                chatRoomRepository.save(toJoinRoom);
            }
        }

        model.addAttribute("room", chatService.findRoomById(roomId));
        model.addAttribute("member", member);

        return "usr/chat/room";
    }

}