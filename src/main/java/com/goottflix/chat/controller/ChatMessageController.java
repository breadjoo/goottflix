package com.goottflix.chat.controller;

import com.goottflix.chat.entity.ChatMessage;
import com.goottflix.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message) {
        // 메시지 타입에 따른 처리 (텍스트 또는 이미지)
        if ("IMAGE".equals(message.getType())) {
            System.out.println(message.getSender() + " 가 채팅방에 이미지 업로드를 했습니다");
        } else {
            System.out.println("보낸사람 : " + message.getSender());
            System.out.println("메시지전송 : " + message.getMessage());
        }

        // 메시지를 데이터베이스에 저장
        chatMessageService.insertChatMessages(message);

        // 메시지를 해당 채팅방 구독자에게 전송
        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomId(), message);
    }

    @RestController
    @RequestMapping("/api/message")
    public static class MessageRestController {

        private final ChatMessageService chatMessageService;

        public MessageRestController(ChatMessageService chatMessageService) {
            this.chatMessageService = chatMessageService;
        }

        @GetMapping("/{roomId}")
        public List<ChatMessage> getMessagesByRoomId(@PathVariable Long roomId) {
            return chatMessageService.getMessagesByRoomId(roomId);
        }



    }
}