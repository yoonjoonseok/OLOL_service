package com.ll.olol.boundedContext.chat.repository;

import com.ll.olol.boundedContext.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.roomHost")
    List<ChatRoom> findAll();

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.chatMembers cm JOIN FETCH cr.roomHost WHERE cm.username = :username")
    List<ChatRoom> findByChatRoom_Username(@Param("username") String username);

    Optional<ChatRoom> findByRoomId(String roomId);

    Optional<ChatRoom> findByRoomName(String roomName);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.roomHost.id = :memberId")
    List<ChatRoom> findByRoomHostId(@Param("memberId") Long memberId);

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.chatMembers cm WHERE cm.id = :userId")
    List<ChatRoom> findChatRoomsByMemberId(@Param("userId") Long userId);

}
