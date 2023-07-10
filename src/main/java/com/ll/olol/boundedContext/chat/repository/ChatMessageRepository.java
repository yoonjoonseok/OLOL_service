package com.ll.olol.boundedContext.chat.repository;

import com.ll.olol.boundedContext.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm JOIN FETCH cm.sender WHERE cm.chatRoom.roomId = :roomId")
    List<ChatMessage> findByChatRoom_RoomId(@Param("roomId") String roomId);

}