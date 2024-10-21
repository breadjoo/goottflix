package com.goottflix.chat.controller;

import com.goottflix.chat.entity.ChatRoom;
import com.goottflix.chat.service.ChatRoomService;
import com.goottflix.user.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JWTUtil jwtUtil;

    @GetMapping
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomService.getAllChatRooms();
    }

    @GetMapping("/{id}")
    public ChatRoom getChatRoomById(@PathVariable Long id) {
        return chatRoomService.getChatRoomById(id);
    }

    @PostMapping("/createroom")
    public ChatRoom createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomService.createChatRoom(chatRoom);
    }


    @GetMapping("/getRole")
    public String getUserRole(Authentication authentication) {
        if (authentication == null) {
            return "ROLE_USER";  // 인증되지 않은 사용자는 기본적으로 USER로 취급
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.equals("ROLE_ADMIN"))
                .findAny()
                .orElse("ROLE_USER");  // ROLE_ADMIN 권한이 없으면 USER로 반환
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.noContent().build();  // 성공 시 204 No Content 반환
    }


    @GetMapping("/getusername")
    public ResponseEntity<?> userName(@CookieValue("Authorization") String token) {
        String username = jwtUtil.getUsername(token);
        System.out.println("username = " + username);
        return ResponseEntity.ok(Collections.singletonMap("username", username));
    }


    @GetMapping("/{id}/name")
    public ResponseEntity<?> getChatRoomNameById(@PathVariable Long id) {
        String roomName = chatRoomService.getChatRoomNameById(id);
        return ResponseEntity.ok(roomName);
    }


}
