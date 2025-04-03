package com.project.usedplatform.chat.chatroom;

import com.project.usedplatform.chat.ChatMessage;
import com.project.usedplatform.chat.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/room")
    public String chatRoom(@RequestParam("receiver") String receiver,
                           Model model,
                           Principal principal) {

        // 현재 로그인한 사용자와, 채팅 상대(receiver)를 설정
        String userA = principal.getName();
        String userB = receiver;

        // 두 사용자 간 채팅방을 조회하거나 생성
        ChatRoom room = chatRoomService.getOrCreateChatRoom(userA, userB);

        // 해당 채팅방의 메시지 목록 조회
        List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByTimestamp(room.getId());

        // 템플릿에 전달할 값 설정
        model.addAttribute("sender", userA);
        model.addAttribute("receiver", userB);
        model.addAttribute("roomId", room.getId());
        model.addAttribute("messages", messages);

        return "chat/chat"; // 채팅창 템플릿 호출
    }
}