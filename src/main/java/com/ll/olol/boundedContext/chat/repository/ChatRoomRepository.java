package com.ll.olol.boundedContext.chat.repository;

import com.ll.olol.boundedContext.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.sender JOIN FETCH cr.receiver")
    List<ChatRoom> findAll();

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.sender crs JOIN FETCH cr.receiver crr")
    List<ChatRoom> findByChatRoom_Username(@Param("username") String username);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.roomId = :roomId")
    Optional<ChatRoom> findByRoomId(@Param("roomId") String roomId);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.sender.id = :senderId AND cr.receiver.id = :receiverId")
    Optional<ChatRoom> findExistChatRoom(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);


    @Query("SELECT cr FROM ChatRoom cr  WHERE cr.roomHost.id = :memberId")
    List<ChatRoom> findByRoomHostId(@Param("memberId") Long memberId);

}