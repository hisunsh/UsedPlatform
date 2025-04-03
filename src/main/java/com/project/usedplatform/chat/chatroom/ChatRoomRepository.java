package com.project.usedplatform.chat.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT r FROM ChatRoom r " +
            "WHERE ((r.buyer = :userA AND r.seller = :userB) OR (r.buyer = :userB AND r.seller = :userA))")
    ChatRoom findRoomByUsers(@Param("userA") String userA, @Param("userB") String userB);

    // 아래 메서드가 추가되어야, 양쪽 사용자와 관련된 채팅방 목록을 조회할 수 있습니다.
    List<ChatRoom> findByBuyerOrSeller(String buyer, String seller);
}