package com.ll.olol.boundedContext.chat.controller;

import com.ll.olol.base.rq.Rq;
import com.ll.olol.boundedContext.chat.dto.ChatRoomDetailDTO;
import com.ll.olol.boundedContext.chat.entity.ChatMessage;
import com.ll.olol.boundedContext.chat.service.ChatService;
import com.ll.olol.boundedContext.member.entity.Member;
import com.ll.olol.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;
    private final Rq rq;

    //나의 채팅방 목록 조회
    @GetMapping("/rooms")
//    @PreAuthorize("hasRole('member')")
    public String myRooms(@AuthenticationPrincipal User user, Model model) {
        String nickname = rq.getMember().getNickname();
        List<ChatRoomDetailDTO> roomList = chatService.findByNickname(nickname);
        model.addAttribute("myNickname", rq.getMember().getNickname());
        model.addAttribute("roomList", roomList);

        for (ChatRoomDetailDTO list : roomList) {
            System.out.println(list);
        }
        return "usr/chat/chatList";
    }

    //채팅방 개설
//    @PostMapping("/room/{id}")
//    @PreAuthorize("hasRole('member')")
//    public String create(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
//
//        Member member1 = memberService.findByUsername(user.getUsername()).orElse(null);
//
//        return rq.redirectWithMsg("/chat/rooms", "채팅방이 개설되었습니다.");
//    }

    @GetMapping("/create")
    public String createRoom(@AuthenticationPrincipal User user) {
        Member m1 = null;
        Member m2 = null;
        chatService.createChatRoomDetailDTO(m1, m2, rq.getMember());

        return rq.redirectWithMsg("/chat/rooms", "채팅방이 개설되었습니다.");
    }

    //채팅방 상세
    @GetMapping("/room/{roomId}")
//    @PreAuthorize("hasRole('member')")
    public String getRoom(@PathVariable String roomId, Model model, @AuthenticationPrincipal User user) {

        List<ChatMessage> messages = chatService.findByRoomId(roomId);
        Member member = memberService.findByUsername(user.getUsername()).orElse(null);


        if (member == null) {
            return rq.redirectWithMsg("/user/member/login", "다시 로그인해주세요!");
        }


        model.addAttribute("room", chatService.findRoomById(roomId));
//        model.addAttribute("messages", messages);
        model.addAttribute("member", member);
//        model.addAttribute("admin", "관리자");

        return "usr/chat/room";
    }


}