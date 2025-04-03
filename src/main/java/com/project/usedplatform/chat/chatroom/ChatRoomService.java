package com.project.usedplatform.chat.chatroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    // 두 사용자 간의 채팅방을 조회하거나 없으면 생성
    public ChatRoom getOrCreateChatRoom(String userA, String userB) {
        ChatRoom room = chatRoomRepository.findRoomByUsers(userA, userB);
        if (room == null) {
            room = new ChatRoom();
            room.setBuyer(userA);
            room.setSeller(userB);
            room = chatRoomRepository.save(room);
        }
        return room;
    }

    // 특정 사용자가 참여한 채팅방 목록 조회
    public List<ChatRoom> getChatRoomsForUser(String username) {
        // buyer 또는 seller가 username인 경우 모두 반환
        return chatRoomRepository.findByBuyerOrSeller(username, username);
    }

    public void leaveChatRoom(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }
}